package io.github.diegoflassa.template_android_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import io.github.diegoflassa.template_android_app.R
import io.github.diegoflassa.template_android_app.data.entities.Patient
import io.github.diegoflassa.template_android_app.databinding.RecyclerviewItemLoadingBinding
import io.github.diegoflassa.template_android_app.databinding.RecyclerviewItemPatientBinding
import io.github.diegoflassa.template_android_app.ui.allPatients.AllPatientsFragmentDirections
import java.text.SimpleDateFormat

open class AllPatientsAdapter(
    var patients: List<Patient>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_PATIENT = 0
        private const val TYPE_LOADING = 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            0 -> {
                val binding = RecyclerviewItemPatientBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                viewHolder = PatientViewHolder(binding.root)
            }
            1 -> {
                val binding = RecyclerviewItemLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                viewHolder = LoadingViewHolder(binding.root)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

        return viewHolder
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val element = patients[position]
        when (holder) {
            is PatientViewHolder -> holder.bind(element)
            is LoadingViewHolder -> holder.bind()
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val patient = patients[position]
        return when {
            patient.fullName.first.isEmpty() -> {
                TYPE_LOADING
            }
            patient.fullName.first.isNotEmpty() -> {
                TYPE_PATIENT
            }
            else -> {
                throw IllegalArgumentException("Invalid type of data $position")
            }
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
        }
    }

    class PatientViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = RecyclerviewItemPatientBinding.bind(itemView)
        fun bind(
            patient: Patient?,
        ) {
            val resources = itemView.resources

            if (patient != null && !patient.id.value.isNullOrEmpty()) {
                // Load image
                binding.rviAvatar.load(patient.picture.mediumUrl) {
                    placeholder(R.drawable.image_placeholder)
                    transformations(CircleCropTransformation())
                }
                binding.rviName.text =
                    resources.getString(R.string.rvi_name, patient.fullName.getFullName())
                binding.rviGender.text = patient.gender
                binding.rviCountry.text = patient.nationality
                val formattedBirthdate: String =
                    if (patient.dateOfBirth.getDateAsDateTime() != null) {
                        val simpleDateFormat =
                            SimpleDateFormat.getDateInstance(SimpleDateFormat.DATE_FIELD)
                        simpleDateFormat.format(patient.dateOfBirth.getDateAsDateTime()!!)
                    } else {
                        resources.getString(R.string.unavailable)
                    }
                binding.rviDob.text = formattedBirthdate
            }
            // Click listener
            itemView.setOnClickListener {
                it.findNavController().navigate(
                    AllPatientsFragmentDirections.actionNavHomeToPatientDetailsDialogFragment(
                        patient
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return patients.size
    }
}
