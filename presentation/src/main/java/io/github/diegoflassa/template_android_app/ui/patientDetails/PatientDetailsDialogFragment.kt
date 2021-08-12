package io.github.diegoflassa.template_android_app.ui.patientDetails

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import io.github.diegoflassa.template_android_app.R
import io.github.diegoflassa.template_android_app.databinding.FragmentPatientDetailsBinding
import io.github.diegoflassa.template_android_app.helper.viewLifecycle
import io.github.diegoflassa.template_android_app.models.PatientDetailsFragmentViewModel
import java.text.SimpleDateFormat

private const val ARG_PATIENT = "patient"

/**
 * A simple [DialogFragment] subclass.
 */
//@AndroidEntryPoint
class PatientDetailsDialogFragment : DialogFragment() {

    private var binding: FragmentPatientDetailsBinding by viewLifecycle()
    lateinit var viewModel: PatientDetailsFragmentViewModel// by viewModels()
    private val args: PatientDetailsDialogFragmentArgs by navArgs()

    /** The system calls this only when creating the layout in a dialog.  */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(PatientDetailsFragmentViewModel::class.java)
        viewModel.patient = args.patient
        binding = FragmentPatientDetailsBinding.inflate(activity!!.layoutInflater, null, false)
         binding.close.setOnClickListener {
            it.findNavController().navigateUp()
        }
        Log.i(tag, "PatientDetailsDialogFragment.onCreateDialog")
        showPatient()
        return binding.root
    }

    private fun showPatient() {
        if (viewModel.patient != null) {
            binding.avatar.load(viewModel.patient!!.picture.largeUrl) {
                placeholder(R.drawable.image_placeholder)
                transformations(CircleCropTransformation())
            }
            var sanitizedFullName = "Unavailable"
            if (viewModel.patient!!.fullName.getFullName().isNotEmpty()) {
                sanitizedFullName = viewModel.patient!!.fullName.getFullName()
            }
            binding.fullName.text = sanitizedFullName
            var sanitizedTelephone = "Unavailable"
            if (viewModel.patient!!.telephone.isNotEmpty()) {
                sanitizedTelephone = viewModel.patient!!.telephone
            }
            binding.telephone.text = getString(R.string.dt_telephone, sanitizedTelephone)
            var sanitizedCellphone = "Unavailable"
            if (viewModel.patient!!.cellphone.isNotEmpty()) {
                sanitizedCellphone = viewModel.patient!!.cellphone
            }
            binding.cellphone.text = getString(R.string.dt_cellphone, sanitizedCellphone)
            var sanitizedGender = "Unavailable"
            if (viewModel.patient!!.gender.isNotEmpty()) {
                sanitizedGender = viewModel.patient!!.gender
            }
            binding.gender.text = getString(R.string.dt_gender, sanitizedGender)
            var sanitizedNationality = "Unavailable"
            if (viewModel.patient!!.nationality.isNotEmpty()) {
                sanitizedNationality = viewModel.patient!!.nationality
            }
            binding.nationality.text = getString(R.string.dt_nationality, sanitizedNationality)
            val formattedBirthdate: String =
                if (viewModel.patient!!.dateOfBirth.getDateAsDateTime() != null) {
                    val simpleDateFormat =
                        SimpleDateFormat.getDateInstance(SimpleDateFormat.DATE_FIELD)
                    simpleDateFormat.format(viewModel.patient!!.dateOfBirth.getDateAsDateTime()!!)
                } else {
                    resources.getString(R.string.unavailable)
                }
            binding.dob.text = getString(R.string.dt_dob, formattedBirthdate)
            var sanitizedAddress = "Unavailable"
            if (viewModel.patient!!.address.getFullAddress().isNotEmpty()) {
                sanitizedAddress = viewModel.patient!!.address.getFullAddress()
            }
            binding.address.text = getString(R.string.dt_address, sanitizedAddress)
            Log.i(PatientDetailsDialogFragment.tag, "PatientDetailsFragment.showPatient")
        }
    }

    companion object {
        private val tag = PatientDetailsDialogFragment::class.simpleName
    }
}
