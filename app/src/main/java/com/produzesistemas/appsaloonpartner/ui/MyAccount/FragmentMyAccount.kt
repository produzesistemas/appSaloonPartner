package com.produzesistemas.appsaloonpartner.ui.MyAccount

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.view.*
import android.content.Intent
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.produzesistemas.appsaloonpartner.LoginActivity
import com.produzesistemas.appsaloonpartner.R
import com.produzesistemas.appsaloonpartner.database.DataSourceUser
import com.produzesistemas.appsaloonpartner.databinding.FragmentMyAccountBinding
import com.produzesistemas.appsaloonpartner.model.Token
import com.produzesistemas.appsaloonpartner.model.Type
import com.produzesistemas.appsaloonpartner.utils.MainUtils
import com.produzesistemas.appsaloonpartner.viewmodel.LoginViewModel
import com.produzesistemas.appsaloonpartner.viewmodel.ViewModelMain
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*


class FragmentMyAccount : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentMyAccountBinding
    private lateinit var viewModelMain: ViewModelMain
    private var mSearchItem: MenuItem? = null
    val nFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    private var sv: SearchView? = null
    private var datasource: DataSourceUser? = null
    private lateinit var user: Token

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.fragment_my_account,
                container,
                false
        )
        return binding.root
    }


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true);
        datasource = context?.let { DataSourceUser(it) }
        user = datasource?.get()!!
        if (user.token == "") {
            changeActivity()
        } else {
            binding.textViewEmail.text = user.email
        }
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        activity?.run {
            viewModelMain = ViewModelProvider(this).get(ViewModelMain::class.java)
        } ?: throw Throwable("invalid activity")
        viewModelMain.updateActionBarTitle(getString(R.string.menu_my_account))

        loginViewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it.code == 401) {
                MainUtils.snack(view, it.message, Snackbar.LENGTH_LONG)
                changeActivity()
            }
        }

        loginViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        loginViewModel.complete.observe(viewLifecycleOwner, Observer {
            if (it) {
//                clientViewModel.getAll(token.token)
            }
        })

        loginViewModel.establishment.observe(viewLifecycleOwner, Observer {
            binding.textViewName.text = it.name
            binding.textViewCnpj.text = it.cnpj
            binding.textViewAddress.text = it.address
            binding.textViewResponsible.text = it.responsible
            binding.textViewDescriptionEstablishment.text = it.description
            Picasso.get()
                .load(MainUtils.urlImage + it.imageName)
                .fit().centerCrop()
                .into(binding.imageViewEstablisment)
            loadTypes(it.typeId)
        })
        loginViewModel.getMyAccount(user.token)
    }

    private fun changeActivity() {
        activity?.let{
            datasource!!.deleteAll()
            val intent = Intent (it, LoginActivity::class.java)
            it.startActivity(intent)
        }
    }

    private fun loadTypes(id: Int) {
        val res = resources
        val forms = res.getStringArray(R.array.ArrayType)
        forms.forEach {
            val s = it.split(",")
            val form = Type()
            form.id = s[0].toInt()
            form.description = s[1]
            if (
                form.id == id
            ){
                binding.textViewTypeId.text = form.description
            }
        }
    }


}



