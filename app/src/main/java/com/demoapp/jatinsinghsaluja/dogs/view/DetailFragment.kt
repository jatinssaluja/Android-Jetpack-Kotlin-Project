package com.demoapp.jatinsinghsaluja.dogs.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

import com.demoapp.jatinsinghsaluja.dogs.R
import com.demoapp.jatinsinghsaluja.dogs.databinding.FragmentDetailBinding
import com.demoapp.jatinsinghsaluja.dogs.model.DogBreed
import com.demoapp.jatinsinghsaluja.dogs.util.getProgressDrawable
import com.demoapp.jatinsinghsaluja.dogs.util.loadImage
import com.demoapp.jatinsinghsaluja.dogs.viewmodel.DetailViewModel
import com.demoapp.jatinsinghsaluja.dogs.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_list.*


/**
 * A simple [Fragment] subclass.
 *
 */
class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var dataBinding: FragmentDetailBinding
    private var dogUuid = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.fetch(dogUuid)

        observeViewModel()

    }

    private fun observeViewModel() {

        viewModel.dogLiveData.observe(this, Observer { dogLiveData:DogBreed ->

            dogLiveData?.let {

                dataBinding.dog = dogLiveData
            }

        })

    }


}
