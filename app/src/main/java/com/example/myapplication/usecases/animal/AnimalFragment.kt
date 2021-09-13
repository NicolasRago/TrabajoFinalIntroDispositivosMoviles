package com.example.myapplication.usecases.animal

import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.example.myapplication.R
import android.widget.*

import com.example.myapplication.model.AnimalInfo
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import android.app.DatePickerDialog

import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat

class AnimalFragment : Fragment() {

    private lateinit var viewModel: AnimalViewModel

    private lateinit var animalIdTextInputLayout: TextInputLayout
    private lateinit var searchButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var herdTextView: TextView
    private lateinit var birthDateTextView: TextView
    private lateinit var lastDateBirthTextView: TextView
    private lateinit var birthCountTextView: TextView
    private lateinit var weightTextView: TextView
    private lateinit var bcsTextView: TextView
    private lateinit var addButton: FloatingActionButton
    private lateinit var datePicker: DatePickerDialog
    private lateinit var informationLinearLayout: LinearLayoutCompat
    private lateinit var alertBtn: Button
    private lateinit var bcsMinTil: TextInputLayout
    private lateinit var bcsMaxTil: TextInputLayout
    private lateinit var alertProgressBar: ProgressBar

    private var animalSelected: AnimalInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.animal_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        herdTextView = view.findViewById(R.id.tv_herd)
        birthDateTextView = view.findViewById(R.id.tv_fecha_nacimiento)
        lastDateBirthTextView = view.findViewById(R.id.tv_ultima_fecha_parto)
        birthCountTextView = view.findViewById(R.id.tv_cantidad_partos)
        weightTextView = view.findViewById(R.id.tv_peso)
        bcsTextView = view.findViewById(R.id.tv_last_bcs)
        progressBar = view.findViewById(R.id.progress_bar_animal)
        animalIdTextInputLayout = view.findViewById(R.id.add_animal_id_til)
        searchButton = view.findViewById(R.id.animal_search_btn)
        addButton = view.findViewById(R.id.add_btn)
        informationLinearLayout = view.findViewById(R.id.animal_information_Linear_layout)
        alertProgressBar = view.findViewById(R.id.animal_alert_progress)
        bcsMinTil = view.findViewById(R.id.animal_bcs_min_til)
        bcsMaxTil = view.findViewById(R.id.animal_bcs_max_til)
        alertBtn = view.findViewById(R.id.animal_alert_btn)

        this.addButton.setOnClickListener { _: View? ->
            createAddAnimalDIalog()
        }

        this.searchButton.setOnClickListener { _: View? ->
            val id = animalIdTextInputLayout.editText?.text.toString()
            if (!id.equals("")){
                viewModel.getAnimal(id)
            } else {
                Toast.makeText(requireContext(), "Debe ingresar un id.", Toast.LENGTH_SHORT).show()
            }
        }

        this.alertBtn.setOnClickListener { _: View? ->
            val minValue = bcsMinTil.editText?.text.toString()
            val maxValue = bcsMaxTil.editText?.text.toString()
            if (!minValue.equals("") && !maxValue.equals("")) {
                viewModel.registerAlert(animalSelected!!, maxValue, minValue)
            } else {
                Toast.makeText(requireContext(), "Debe ingresar el bcs max y bcs min.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AnimalViewModel::class.java)

        viewModel.animal.observe(viewLifecycleOwner, {
            animalSelected = it
            herdTextView.text = it?.herdId.toString()
            birthDateTextView.text = it?.dateBirth.toString()
            lastDateBirthTextView.text = if (it?.lastBirthDate != null) it?.lastBirthDate.toString() else "-"
            birthCountTextView.text = it?.birthCount.toString()
            weightTextView.text = it?.weight.toString()
            bcsTextView.text = it?.cc.toString()
            informationLinearLayout.visibility = View.VISIBLE
        })

        viewModel.loading.observe(viewLifecycleOwner, {
            if (it){
                alertProgressBar.visibility = View.VISIBLE
                informationLinearLayout.visibility = View.GONE
            }else {
                alertProgressBar.visibility = View.GONE
            }
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

        viewModel.message.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun createAddAnimalDIalog(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_animal_dialog);

        val weigth = dialog.findViewById<TextInputLayout>(R.id.add_animal_weight_til)
        val electronidId = dialog.findViewById<TextInputLayout>(R.id.add_animal_electronic_id_til)
        val birthCount = dialog.findViewById<TextInputLayout>(R.id.add_animal_birth_count_til)
        val birthDate = dialog.findViewById<TextInputLayout>(R.id.add_animal_birth_date_til)
        val birthDateEditText = dialog.findViewById<TextInputEditText>(R.id.add_animal_birth_date_tiet)
        val lastBirthDate = dialog.findViewById<TextInputLayout>(R.id.add_animal_last_birth_date_til)
        val lastBirthDateEditText = dialog.findViewById<TextInputEditText>(R.id.add_animal_last_birth_date_tiet)
        val herdTextInputLayout =  dialog.findViewById<TextInputLayout>(R.id.add_animal_herd_til)

        birthDateEditText.setOnClickListener{ _: View? ->
            getDate(birthDate)
        }

        lastBirthDateEditText.setOnClickListener{ _: View? ->
            getDate(lastBirthDate)
        }

        dialog.findViewById<Button>(R.id.cancel_btn).setOnClickListener { _: View? ->
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.accept_btn).setOnClickListener { _: View? ->
            val herdId = herdTextInputLayout.editText?.text.toString()
            if (herdId != ""){
                viewModel.registerAnimal(
                    AnimalInfo(id = null,
                        birthCount = birthCount.editText?.text.toString().toLong(),
                        electronicId = electronidId.editText?.text.toString().toLong(),
                        dateBirth = SimpleDateFormat("yyyy-MM-dd").parse(birthDate.editText?.text.toString()),
                        lastBirthDate = if (!lastBirthDateEditText.text.toString().equals("")) SimpleDateFormat("yyyy-MM-dd").parse(lastBirthDateEditText.text.toString())
                                        else null,
                        herdId = herdId.toLong(),
                        weight = weigth.editText?.text.toString().toFloat(),
                        cc = 0.0f,
                        fechaBcs = null,
                        cowBcsId = 0
                    )
                )
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Debe seleccionar un rodeo.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show();
    }

    private fun getDate(fecha: TextInputLayout) {
        val c = Calendar.getInstance()

        val mes = c.get(Calendar.MONTH)
        val dia = c.get(Calendar.DAY_OF_MONTH)
        val anio = c.get(Calendar.YEAR)

        datePicker = DatePickerDialog(requireContext(), { view, year, month, dayOfMonth ->
                val mesActual = month + 1
                val dia = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
                val mes = if (mesActual < 10) "0$mesActual" else mesActual.toString()

                fecha.editText?.setText("${year}-${mes}-${dia}")
            },
            anio, mes, dia
        )
        datePicker.show()
    }
}