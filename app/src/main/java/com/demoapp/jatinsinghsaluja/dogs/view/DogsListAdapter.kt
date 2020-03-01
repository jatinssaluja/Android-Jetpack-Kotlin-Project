package com.demoapp.jatinsinghsaluja.dogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.demoapp.jatinsinghsaluja.dogs.R
import com.demoapp.jatinsinghsaluja.dogs.databinding.ItemDogBinding
import com.demoapp.jatinsinghsaluja.dogs.model.DogBreed
import com.demoapp.jatinsinghsaluja.dogs.util.getProgressDrawable
import com.demoapp.jatinsinghsaluja.dogs.util.loadImage
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter(val dogsList: ArrayList<DogBreed>): RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(), DogItemClickListener{

    fun updateDogList(newDogsList:List<DogBreed>){

        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {

        val inflator = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemDogBinding>(inflator, R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount(): Int  = dogsList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {

        holder.view.dog = dogsList[position]
        holder.view.listener = this

    }

    override fun onDogItemClicked(v: View) {

        val uuid = v.dogId.text.toString().toInt()
        val action = ListFragmentDirections.actionDetailFragment()
        action.dogUuid = uuid
        Navigation.findNavController(v).navigate(action)
    }

    class DogViewHolder(var view: ItemDogBinding): RecyclerView.ViewHolder(view.root)
}