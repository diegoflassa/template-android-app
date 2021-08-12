package io.github.diegoflassa.template_android_app.ui.allPatients

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.diegoflassa.template_android_app.R
import io.github.diegoflassa.template_android_app.adapters.AllPatientsAdapter
import io.github.diegoflassa.template_android_app.databinding.FragmentAllPatientsBinding
import io.github.diegoflassa.template_android_app.enums.Gender
import io.github.diegoflassa.template_android_app.enums.QueryFields
import io.github.diegoflassa.template_android_app.helper.viewLifecycle
import io.github.diegoflassa.template_android_app.interfaces.OnSearch
import io.github.diegoflassa.template_android_app.models.AllPatientsFragmentViewModel
import io.github.diegoflassa.template_android_app.ui.searchBar.SearchBarFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.lang.ref.WeakReference
import androidx.recyclerview.widget.RecyclerView


/**
 * A simple [Fragment] subclass.
 * Use the [AllPatientsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@DelicateCoroutinesApi
@Suppress("unused")
//@AndroidEntryPoint
@Parcelize
class AllPatientsFragment : Fragment(), OnSearch {

    @IgnoredOnParcel
    private var binding: FragmentAllPatientsBinding by viewLifecycle()

    @IgnoredOnParcel
    private lateinit var viewModel: AllPatientsFragmentViewModel // by viewModels()

    @IgnoredOnParcel
    private var adapter: WeakReference<AllPatientsAdapter>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val bundle = bundleOf(SearchBarFragment.key_on_search to this)
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add<SearchBarFragment>(R.id.fragment_search_bar_container, args = bundle)
            }
        }
    }

    @DelicateCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(AllPatientsFragmentViewModel::class.java)
        binding = FragmentAllPatientsBinding.inflate(inflater, container, false)
        val swipeContainer =
            binding.root.findViewById(R.id.swipeContainerAllPatients) as SwipeRefreshLayout
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener {
            viewModel.reloadData()
            Log.i(AllPatientsFragment.tag, "AllPatientsFragment.onRefreshListener")
        }
        initRecyclerView()
        val mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerview.layoutManager = mLayoutManager
        val mScrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!viewModel.isLoading) {
                        val visibleItemCount: Int = mLayoutManager.childCount
                        val totalItemCount: Int = mLayoutManager.itemCount
                        val pastVisibleItems: Int = mLayoutManager.findFirstVisibleItemPosition()
                        if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                            viewModel.getNextPage()
                        }
                    }
                }
            }
        binding.recyclerview.addOnScrollListener(mScrollListener)
        viewModel.patientsLiveData.observe(
            viewLifecycleOwner,
            {
                updateAdapter()
                swipeContainer.isRefreshing = false
                hideLoadingScreen()
                Log.i(
                    AllPatientsFragment.tag,
                    "AllPatientsFragment.patientsLiveData.observe: Data updated!"
                )
            }
        )
        showLoadingScreen()
        Log.i(AllPatientsFragment.tag, "AllPatientsFragment.onCreateView")
        return binding.root
    }

    private fun initRecyclerView() {
        val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.card_item_divider
            )!!
        )
        binding.recyclerview.addItemDecoration(itemDecoration)
        updateAdapter()
        Log.i(AllPatientsFragment.tag, "AllPatientsFragment:initRecyclerView")
    }

    private fun showLoadingScreen() {
        binding.allPatientsProgress.visibility = View.VISIBLE
        Log.i(AllPatientsFragment.tag, "AllPatientsFragment.showLoadingScreen")
    }

    private fun hideLoadingScreen() {
        binding.allPatientsProgress.visibility = View.GONE
        Log.i(AllPatientsFragment.tag, "AllPatientsFragment.hideLoadingScreen")
    }

    private fun updateAdapter() {
        if (adapter == null) {
            adapter = WeakReference(AllPatientsAdapter(viewModel.patients))
            adapter!!.get()!!.stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
            binding.recyclerview.adapter = adapter!!.get()
        } else {
            adapter!!.get()!!.patients = viewModel.patients
            adapter!!.get()!!.notifyDataSetChanged()
        }
        Log.i(AllPatientsFragment.tag, "AllPatientsFragment.updateAdapter")
    }

    companion object {
        private val tag = AllPatientsFragment::class.simpleName

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AllPatientsFragment.
         */
        @JvmStatic
        fun newInstance() =
            AllPatientsFragment()
    }

    override fun onSearch(query: String?, nationality: String?, gender: Gender) {
        val queryFields = HashMap<QueryFields, String>()
        if (query != null && query.isNotEmpty()) {
            queryFields[QueryFields.FULL_NAME] = query
        }
        if (nationality != null && nationality.isNotEmpty() && nationality != "All") {
            queryFields[QueryFields.NATIONALITY] = nationality
        }
        if (gender != Gender.UNKNOWN) {
            queryFields[QueryFields.GENDER] = gender.toString()
        }
        if (queryFields.isNotEmpty()) {
            viewModel.queryFields = queryFields
            viewModel.isFromQuery = true
            viewModel.search()
        }
    }

    override fun clear() {
        viewModel.isFromQuery = false
        viewModel.clear()
    }
}
