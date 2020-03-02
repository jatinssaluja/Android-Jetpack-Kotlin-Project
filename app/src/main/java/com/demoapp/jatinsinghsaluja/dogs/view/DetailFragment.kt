package com.demoapp.jatinsinghsaluja.dogs.view


import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.demoapp.jatinsinghsaluja.dogs.R
import com.demoapp.jatinsinghsaluja.dogs.databinding.FragmentDetailBinding
import com.demoapp.jatinsinghsaluja.dogs.databinding.SendSmsDialogBinding
import com.demoapp.jatinsinghsaluja.dogs.model.DogBreed
import com.demoapp.jatinsinghsaluja.dogs.model.DogPalette
import com.demoapp.jatinsinghsaluja.dogs.model.SmsInfo
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
    private var sendSMSStarted = false
    private var currentDog: DogBreed? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

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

        viewModel.dogLiveData.observe(this, Observer { dogLiveData ->

            dogLiveData?.let {

                dataBinding.dog = dogLiveData
                currentDog = dogLiveData
                it.imageUrl?.let {
                    setBackgroundColor(it)
                }
            }

        })

    }

    private fun setBackgroundColor(url: String) {

        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource).generate { palette ->
                        val intColor = palette?.lightMutedSwatch?.rgb ?: 0
                        val myPalette = DogPalette(intColor)

                        // attaches myPalette to the layout
                        dataBinding.palette = myPalette
                    }
                }

            })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send_sms -> {
                sendSMSStarted = true
                (activity as MainActivity).checkSMSPermission()
            }
            R.id.action_share -> {

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT,"Check out this dog breed")
                intent.putExtra(Intent.EXTRA_TEXT,"${currentDog?.dogBreed} bred for ${currentDog?.bredFor}")
                intent.putExtra(Intent.EXTRA_STREAM,currentDog?.imageUrl)
                startActivity(Intent.createChooser(intent,"Share with"))

            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean) {

        if (sendSMSStarted && permissionGranted) {

            context?.let {
                val smsInfo =
                    SmsInfo("", "${currentDog?.dogBreed} bred for ${currentDog?.bredFor}", currentDog?.imageUrl)

                val dialogBinding = DataBindingUtil.inflate<SendSmsDialogBinding>(
                    LayoutInflater.from(it),
                    R.layout.send_sms_dialog,
                    null,
                    false
                )

                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS") { dialog, which ->
                        if (!dialogBinding.smsDestination.text.isNullOrEmpty()) {
                            smsInfo.to = dialogBinding.smsDestination.text.toString()
                            sendSms(smsInfo)
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, which -> }
                    .show()

                dialogBinding.smsInfo = smsInfo
            }
        }

    }

    private fun sendSms(smsInfo: SmsInfo) {
        val intent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(context, 0, intent, 0)
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(smsInfo.to, null, smsInfo.text, pi, null)
    }


}
