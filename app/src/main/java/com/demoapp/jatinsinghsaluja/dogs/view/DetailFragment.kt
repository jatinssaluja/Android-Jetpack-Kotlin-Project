package com.demoapp.jatinsinghsaluja.dogs.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

import com.demoapp.jatinsinghsaluja.dogs.R
import com.demoapp.jatinsinghsaluja.dogs.model.DogBreed
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
    private var dogUuid = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.fetch()

        observeViewModel()

        arguments?.let {

            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
        }



    }

    private fun observeViewModel() {

        viewModel.dogLiveData.observe(this, Observer { dogLiveData:DogBreed ->

            dogLiveData?.let {

                dogName.text = dogLiveData.dogBreed
                dogPurpose.text = dogLiveData.bredFor
                dogTemperament.text = dogLiveData.temperament
                dogLifespan.text = dogLiveData.lifeSpan

            }

        })

    }


}
