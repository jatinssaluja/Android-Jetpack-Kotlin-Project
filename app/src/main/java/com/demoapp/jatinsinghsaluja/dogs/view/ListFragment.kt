package com.demoapp.jatinsinghsaluja.dogs.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.demoapp.jatinsinghsaluja.dogs.R
import com.demoapp.jatinsinghsaluja.dogs.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*


/**
 * A simple [Fragment] subclass.
 *
 */
class ListFragment : Fragment() {

    private lateinit var viewModel:ListViewModel
    private val dogsListAdapter = DogsListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        // instantiate RecyclerView
        dogsList.apply {

            // allows to arrange elements sequentially in top to bottom fashion
            layoutManager = LinearLayoutManager(context)

            adapter = dogsListAdapter

        }

        refreshLayout.setOnRefreshListener {
            dogsList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingSpinner.visibility = View.VISIBLE
            viewModel.refreshBypassCache()

            // will only make little spinner at the top disappear, not the loading spinner
            refreshLayout.isRefreshing = false
        }

        observeViewModel()

    }


    private fun observeViewModel(){

        viewModel.dogs.observe(this, Observer {dogs ->

            dogs?.let {

                dogsList.visibility = View.VISIBLE
                dogsListAdapter.updateDogList(dogs)
            }

        })

        viewModel.dogsLoadingError.observe(this, Observer { isError->

            isError?.let{
                listError.visibility = if(it) View.VISIBLE else View.GONE

        } })

        viewModel.loading.observe(this, Observer { isLoading ->

            isLoading?.let {

                loadingSpinner.visibility = if(it) View.VISIBLE else View.GONE
                if(it){
                    dogsList.visibility = View.GONE
                    listError.visibility = View.GONE
                }

            }
        })
    }


}
