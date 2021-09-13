package com.example.myapplication.usecases.herd

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.myapplication.R
import com.example.myapplication.model.HerdInfo
import com.google.android.material.textfield.TextInputLayout

class HerdFragment : Fragment() {

    private lateinit var rodeoIdTextInputLayout: TextInputLayout
    private lateinit var searchButton: Button
    private lateinit var locationTextView: TextView
    private lateinit var lastBcsTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var informationLinearLayout: LinearLayoutCompat
    private lateinit var viewModel: HerdViewModel
    private lateinit var bcsMinTil: TextInputLayout
    private lateinit var bcsMaxTil: TextInputLayout
    private lateinit var alertProgressBar: ProgressBar
    private lateinit var alertBtn: Button

    private var herdSelected: HerdInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.herd_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rodeoIdTextInputLayout = view.findViewById(R.id.add_herd_id_til)
        searchButton = view.findViewById(R.id.herd_search_btn)
        locationTextView = view.findViewById(R.id.tv_location)
        lastBcsTextView = view.findViewById(R.id.tv_bcs_promedio)
        progressBar = view.findViewById(R.id.progress_bar_animal)
        informationLinearLayout = view.findViewById(R.id.information_Linear_layout)
        bcsMinTil = view.findViewById(R.id.herd_bcs_min_til)
        bcsMaxTil = view.findViewById(R.id.herd_bcs_max_til)
        alertProgressBar = view.findViewById(R.id.herd_alert_progress)
        alertBtn = view.findViewById(R.id.herd_alert_btn)

        searchButton.setOnClickListener { _: View? ->
            val id = rodeoIdTextInputLayout.editText?.text.toString()
            if (!id.equals("")){
                viewModel.getRodeo(id)
            } else {
                Toast.makeText(requireContext(), "Debe ingresar un id.", Toast.LENGTH_SHORT).show()
            }
        }

        this.alertBtn.setOnClickListener { _: View? ->
            val minValue = bcsMinTil.editText?.text.toString()
            val maxValue = bcsMaxTil.editText?.text.toString()
            if (!minValue.equals("") && !maxValue.equals("")) {
                viewModel.registerAlert(herdSelected!!, maxValue, minValue)
            } else {
                Toast.makeText(requireContext(), "Debe ingresar el bcs max y bcs min.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HerdViewModel::class.java)

        viewModel.loading.observe(viewLifecycleOwner, {
            if (it){
                progressBar.visibility = View.VISIBLE
                informationLinearLayout.visibility = View.GONE
            }else {
                progressBar.visibility = View.GONE
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        viewModel.herd.observe(viewLifecycleOwner, {
            herdSelected = it
            locationTextView.text = it.location.toString()
            lastBcsTextView.text = it.bcsPromedio.toString()
            informationLinearLayout.visibility = View.VISIBLE
        })

        viewModel.alertLoading.observe(viewLifecycleOwner, {
            if (it){
                progressBar.visibility = View.VISIBLE
                alertBtn.isEnabled = false
            }else {
                progressBar.visibility = View.GONE
                alertBtn.isEnabled = true
                bcsMinTil.editText?.setText("")
                bcsMaxTil.editText?.setText("")
            }
        })
    }

}