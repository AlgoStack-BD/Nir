package com.algostack.nir.view.frame
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentHomeBinding
import com.algostack.nir.view.adapter.HomeViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Home : Fragment() {

    private var _binding : FragmentHomeBinding ?= null
    private val binding get() = _binding!!

    private val tabTitle = arrayListOf("Home", "Vila", "Appartment", "Cottage")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater,container,false)


        setTabLayout()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.linearLayout4.setOnClickListener {


            replaceFragmentGenaral(Filter(), Filter::class.java.name)

        }





    }

    private fun setTabLayout() {
        binding.viewPager.adapter = HomeViewPagerAdapter(this)

        TabLayoutMediator(binding.tab, binding.viewPager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        // set this textview on the customView of the TabLayout
        for (i in 0..3) {
            val textView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_title, null) as TextView
            binding.tab.getTabAt(i)?.customView = textView
        }

    }











    private fun replaceFragmentGenaral(fragment: Fragment, flag: String){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragmentConthainerView4,fragment,flag).addToBackStack(flag).commit()

    }








}