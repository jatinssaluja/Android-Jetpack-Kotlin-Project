package com.demoapp.jatinsinghsaluja.dogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.demoapp.jatinsinghsaluja.dogs.R
import com.demoapp.jatinsinghsaluja.dogs.model.DogBreed
import com.demoapp.jatinsinghsaluja.dogs.util.getProgressDrawable
import com.demoapp.jatinsinghsaluja.dogs.util.loadImage
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter(val dogsList: ArrayList<DogBreed>): RecyclerView.Adapter<DogsListAdapter.DogViewHolder>() {

    fun updateDogList(newDogsList:List<DogBreed>){

        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {

        val inflator = LayoutInflater.from(parent.context)
        val view = inflator.inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount(): Int  = dogsList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
         holder.view.name.text = dogsList[position].dogBreed
         holder.view.lifeSpan.text = dogsList[position].lifeSpan
         holder.view.setOnClickListener {

             Navigation.findNavController(it).navigate(ListFragmentDirections.actionDetailFragment())
         }

        holder.view.imageView.loadImage(dogsList[position].imageUrl,
            getProgressDrawable(holder.view.imageView.context))
    }

    class DogViewHolder(var view: View): RecyclerView.ViewHolder(view)
}