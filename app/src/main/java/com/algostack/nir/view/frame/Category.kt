package com.algostack.nir.view.frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentCategoryBinding
import com.algostack.nir.databinding.FragmentContactUsBinding
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.utils.AlertDaialog
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.view.adapter.PublicFeedBestForYouAdapter
import com.algostack.nir.view.adapter.VerticalSpace
import com.algostack.nir.viewmodel.FilterViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Category : Fragment() {


    private var _binding : FragmentCategoryBinding?= null
    private val binding get() =  _binding!!

    private val filterViewModel by viewModels<FilterViewModel> ()


    val bestForYouRecSpace = VerticalSpace()



    private lateinit var bestForYouAdapter: PublicFeedBestForYouAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCategoryBinding.inflate(inflater,container,false)

        bestForYouAdapter = PublicFeedBestForYouAdapter(this::onDetailsCliked)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.BachelorCategory.setOnClickListener {
            // clear the previous data
            bestForYouAdapter.submitList(null)

            filterViewModel.filterbyCategory("Bachelor")
            println("clicked on bachelor")
            bindOvservers()
        }
        binding.familyCategory .setOnClickListener {
            bestForYouAdapter.submitList(null)
            filterViewModel.filterbyCategory("Family")
            println("clicked on Family")
            bindOvservers()
        }

        binding.SubletCategory.setOnClickListener {
            bestForYouAdapter.submitList(null)
            filterViewModel.filterbyCategory("Sublet")
            println("clicked on Family")
            bindOvservers()
        }
        binding.familyandBacholorCategory.setOnClickListener {
            bestForYouAdapter.submitList(null)
            filterViewModel.filterbyCategory("Family & Bachelor")
            println("clicked on Family & Bachelor")
            bindOvservers()
        }

        binding.filteredResult.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.filteredResult.addItemDecoration(bestForYouRecSpace)
        binding.filteredResult.adapter = bestForYouAdapter

        binding.searchAgain.setOnClickListener {
            // findNavController().popBackStack()
            binding.searchResultLayout.isVisible = false
            binding.caatagorylayout.isVisible = true
            binding.textView10.isVisible = true
        }


    }

    private fun onDetailsCliked(publicPostData: PublicPostData) {
        val bundle = Bundle()
        bundle.putString("details", Gson().toJson(publicPostData))

        replaceFragment(PostDetails(),bundle,PostDetails::class.java.simpleName)
    }

    private fun replaceFragment(fragment: Fragment,bundle: Bundle,flag: String? = null){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragment.arguments = bundle
        fragmentTransaction.add(R.id.fragmentConthainerView4,fragment,flag)

        fragmentTransaction.addToBackStack(flag)
        fragmentTransaction.commit()

    }
    private fun bindOvservers() {
        filterViewModel.FilterLiveData.observe(viewLifecycleOwner, Observer {result ->

            when(result){

                is NetworkResult.Success -> {



                    if(result.data!!.status == 200){

                        binding.caatagorylayout.isVisible = false
                        binding.textView10.isVisible = false
                        binding.searchResultLayout.isVisible = true
                        val bestForYouResult = result.data.data

                        bestForYouAdapter.submitList(bestForYouResult)



                    }
                }
                is NetworkResult.Error -> {

                    AlertDaialog.showCustomAlertDialogBox(
                        requireContext(),
                        result.message ?: "Something went wrong"
                    )
                }
                is NetworkResult.Loading -> {
                    //   binding?.logprogressBar?.isVisible = true
                }


                else -> {}
            }



        })


    }


}