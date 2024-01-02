package com.algostack.nir.view.frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentAddBinding
import dagger.hilt.android.AndroidEntryPoint
import hilt_aggregated_deps._com_algostack_nir_viewmodel_AuthViewModel_HiltModules_BindsModule

@AndroidEntryPoint
class add : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // spiner for rent type
        binding.renttypespinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.renttypespinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.renttypespinner.setSelection(position)
//                    Toast.makeText(
//                        requireContext(),
//                        "Selected: ${adapterView?.getItemAtPosition(position).toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }

        // spiner for bead Rooms
        val customBeadroomList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11","12","13","14","15",
            "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50")
        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            customBeadroomList
        )

        binding.beadroomspinner.adapter = adapter

        binding.beadroomspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.beadroomspinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.beadroomspinner.setSelection(position)
//                    Toast.makeText(
//                        requireContext(),
//                        "Selected: ${adapterView?.getItemAtPosition(position).toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }

        // spiner for drawing Rooms
        val customDrawingroomList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11","12","13","14","15",
            "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50")
        val adapter2 = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            customDrawingroomList
        )

        binding.drawingroomspinner.adapter = adapter2

        binding.drawingroomspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.drawingroomspinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.drawingroomspinner.setSelection(position)
//                    Toast.makeText(
//                        requireContext(),
//                        "Selected: ${adapterView?.getItemAtPosition(position).toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }


        // spinner for dining Rooms
        val customDiningroomList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11","12","13","14","15",
            "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50")
        val adapter3 = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            customDiningroomList
        )

        binding.diningroomspinner.adapter = adapter3

        binding.diningroomspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.diningroomspinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.diningroomspinner.setSelection(position)
//                    Toast.makeText(
//                        requireContext(),
//                        "Selected: ${adapterView?.getItemAtPosition(position).toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }


        // spinner for bath Rooms
        val customBathroomList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11","12","13","14","15",
            "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50")
        val adapter4 = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            customBathroomList
        )
        binding.bathroomspinner.adapter = adapter4
        binding.bathroomspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.bathroomspinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.bathroomspinner.setSelection(position)
//                    Toast.makeText(
//                        requireContext(),
//                        "Selected: ${adapterView?.getItemAtPosition(position).toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }

        // spinner for kitchen Rooms
        val customKitchenList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11","12","13","14","15",
            "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50")
        val adapter5 = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            customKitchenList
        )
        binding.kitchenspinner.adapter = adapter5
        binding.kitchenspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.kitchenspinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.kitchenspinner.setSelection(position)
//                    Toast.makeText(
//                        requireContext(),
//                        "Selected: ${adapterView?.getItemAtPosition(position).toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }

        // spinner for balcony Rooms
        val customBalconyList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11","12","13","14","15",
            "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50")
        val adapter6 = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            customBalconyList
        )
        binding.balconyspinner.adapter = adapter6
        binding.balconyspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.balconyspinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.balconyspinner.setSelection(position)
//                    Toast.makeText(
//                        requireContext(),
//                        "Selected: ${adapterView?.getItemAtPosition(position).toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }


//        val checkedRadioButtonId = binding.radioGroup.checkedRadioButtonId // Returns View.NO_ID if nothing is checked.
//       binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
//            // Responds to child RadioButton checked/unchecked
//        }
//
//// To check a radio button
//        binding.radioButton.isChecked = true
//
//// To listen for a radio button's checked/unchecked state changes
//        binding.radioButton.setOnCheckedChangeListener { buttonView, isChecked
//            // Responds to radio button being checked/unchecked
//        }
//
    }
}