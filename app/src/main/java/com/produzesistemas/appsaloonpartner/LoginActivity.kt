package com.produzesistemas.appsaloonpartner

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.produzesistemas.appsaloonpartner.database.DataSourceUser
import com.produzesistemas.appsaloonpartner.databinding.ActivityLoginBinding
import com.produzesistemas.appsaloonpartner.model.Register
import com.produzesistemas.appsaloonpartner.model.Type
import com.produzesistemas.appsaloonpartner.utils.MainUtils
import com.produzesistemas.appsaloonpartner.utils.Mask
import com.produzesistemas.appsaloonpartner.viewmodel.LoginViewModel


class LoginActivity : AppCompatActivity(){
    private var datasource: DataSourceUser? = null
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModelLogin: LoginViewModel
    private lateinit var type: Type
    private var register: Register = Register()

    private val pickImage = 100
    private var imageUri: Uri? = null
    private var types: MutableList<Type> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        datasource = DataSourceUser(this)
        loadTypes(this)
        var token = datasource?.get()!!
        if (token.token != "") {
            changeActivity()
            finish()
        }
        viewModelLogin = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.editTextTelephoneRegister.addTextChangedListener(Mask.insert("(##)#####-####", binding.editTextTelephoneRegister))
        binding.editTextCnpjRegister.addTextChangedListener(Mask.insert("##.###.###.####/##", binding.editTextCnpjRegister))

        binding.radioGroup.check(R.id.radioButtonNo)
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonYes -> {
                    register.scheduling = true
                }
                R.id.radioButtonNo -> {
                    register.scheduling = false
                }
            }
        }

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                type = binding.spinnerType.selectedItem as Type
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }

        binding.cardViewLogin.setOnClickListener{

            if (this?.let { it1 -> MainUtils.isOnline(it1) }!!) {

                if ((binding.editTextEmail.text.toString() == "") || (binding.editTextSecret.text.toString() == "")) {
                    MainUtils.snackInTop(
                        it,
                        this.resources.getString(R.string.validation_login),
                        Snackbar.LENGTH_LONG
                    )
                } else {
                    onLogin(binding.editTextEmail.text.toString(), binding.editTextSecret.text.toString())
                }
            } else {
                MainUtils.snackInTop(it, this.resources.getString(R.string.validation_connection), Snackbar.LENGTH_LONG)
            }
        }

        binding.cardViewSignUp.setOnClickListener{
            binding.scrollView.visibility = View.VISIBLE
            binding.linearLayoutLogin.visibility = View.GONE
           }

        binding.cardViewBack.setOnClickListener{
            binding.linearLayoutRegister.visibility = View.GONE
            binding.linearLayoutLogin.visibility = View.VISIBLE
        }

        binding.cardViewImage.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        binding.cardViewRegister.setOnClickListener{
            if (this?.let { it1 -> MainUtils.isOnline(it1) }!!) {

                if (binding.editTextEstablishment.text.toString() == "") {
                    MainUtils.snackInTop(it,this.resources.getString(R.string.validation_establishment),Snackbar.LENGTH_LONG)
                    return@setOnClickListener
                }

                if (binding.profileImage.drawable == null) {
                    MainUtils.snackInTop(
                        it,
                        this.resources.getString(R.string.validation_image),
                        Snackbar.LENGTH_LONG
                    )
                    return@setOnClickListener

                }


                if ((binding.editTextEmailRegister.text.toString() == "") || (binding.editTextSecretRegister.text.toString() == "")) {
                    MainUtils.snackInTop(
                        it,
                        this.resources.getString(R.string.validation_login),
                        Snackbar.LENGTH_LONG
                    )
                    return@setOnClickListener

                }

                if (binding.editTextSecretRegister.text.toString() != binding.editTextConfirmSecret.text.toString() )  {
                    MainUtils.snackInTop(
                        it,
                        this.resources.getString(R.string.validation_secret_compare),
                        Snackbar.LENGTH_LONG
                    )
                    return@setOnClickListener

                }

                register.email = binding.editTextEmailRegister.text.toString()
                register.secret = binding.editTextSecretRegister.text.toString()
                register.cnpj = binding.editTextCnpjRegister.text.toString()
                register.responsible = binding.editTextResponsableRegister.text.toString()
                register.address = binding.editTextAdressRegister.text.toString()
                register.description = binding.editTextEstablishment.text.toString()
                register.name = binding.editTextEstablishment.text.toString()
                register.description = binding.editTextDescription.text.toString()
                register.phoneNumber =  binding.editTextTelephoneRegister.text.toString()
                register.appName = "AppBeauty"
                register.typeId = type.id
                var bitmap: Bitmap? = (binding.profileImage.drawable as BitmapDrawable).bitmap
                register.base64 = bitmap?.let { it1 -> MainUtils.EncodeImagemTobase64(it1) }.toString()
//                Log.d("AppBeauty",register.toString())




                onRegister(register)


            }
            else {
                MainUtils.snackInTop(it, this.resources.getString(R.string.validation_connection), Snackbar.LENGTH_LONG)
            }
        }

        binding.cardViewForgotPassword.setOnClickListener{
            binding.linearLayoutForgot.visibility = View.VISIBLE
            binding.linearLayoutLogin.visibility = View.GONE
        }

        binding.cardViewForgot.setOnClickListener{
            if (this?.let { it1 -> MainUtils.isOnline(it1) }!!) {
                if ((binding.editTextEmailForgot.text.toString() == "") || (binding.editTextSecretForgot.text.toString() == "")) {
                    MainUtils.snackInTop(
                        it,
                        this.resources.getString(R.string.validation_login),
                        Snackbar.LENGTH_LONG
                    )
                } else {
                    onForgot(binding.editTextEmailForgot.text.toString(), binding.editTextSecretForgot.text.toString())
                }
            } else {
                MainUtils.snackInTop(it, this.resources.getString(R.string.validation_connection), Snackbar.LENGTH_LONG)
            }
        }

        binding.cardViewBackForgot.setOnClickListener{
            binding.linearLayoutForgot.visibility = View.GONE
            binding.linearLayoutLogin.visibility = View.VISIBLE
        }

        viewModelLogin.errorMessage.observe(this) {
            MainUtils.snackInTop(window.decorView.findViewById(android.R.id.content),
                it.message, Snackbar.LENGTH_LONG)
            if (it.code == 401) {
                changeActivity()
            }

//            if (it.code == 600) {
//                startActivity(Intent(this, SubscriptionActivity::class.java))
//            }

            if (it.code == 503) {
                MainUtils.snackInTop(window.decorView.findViewById(android.R.id.content),
                    this.resources.getString(R.string.error_503), Snackbar.LENGTH_LONG)
            }

            binding.imageViewLogin.visibility = View.VISIBLE
            binding.textViewLogin.visibility = View.VISIBLE
        }

        viewModelLogin.loading.observe(this) {
            if (it) {
                binding.imageViewLogin.visibility = View.GONE
                binding.textViewLogin.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.imageViewLogin.visibility = View.VISIBLE
                binding.textViewLogin.visibility = View.VISIBLE
            }
        }

        viewModelLogin.loadingRegister.observe(this) {
            if (it) {
                binding.imageViewRegister.visibility = View.GONE
                binding.textViewRegister.visibility = View.GONE
                binding.progressBarRegister.visibility = View.VISIBLE
            } else {
                binding.progressBarRegister.visibility = View.GONE
                binding.imageViewRegister.visibility = View.VISIBLE
                binding.textViewRegister.visibility = View.VISIBLE
            }
        }

        viewModelLogin.token.observe(this) {
            datasource?.deleteAll()
            datasource?.insert(it)
            changeActivity()
        }

        viewModelLogin.msg.observe(this) {
            MainUtils.snackInTop(
                window.decorView.findViewById(android.R.id.content),
                it, Snackbar.LENGTH_LONG
            )
        }

    }

    override fun onStart() {
        super.onStart()
    }

    private fun loadTypes(context: Context) {
        val res = resources
        val forms = res.getStringArray(R.array.ArrayType)
        forms.forEach {
            val s = it.split(",")
            val form = Type()
            form.id = s[0].toInt()
            form.description = s[1]
            types.add(form)
        }
        val adapterPaymentCondition: ArrayAdapter<Type>? = context?.let { ArrayAdapter<Type>(
            it,
            android.R.layout.simple_spinner_dropdown_item,
            types
        ) }
        adapterPaymentCondition?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = adapterPaymentCondition
    }


    override fun onBackPressed() {
        finishAffinity()
    }

    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 123
    }

    private fun onLogin(email: String, secret: String){
        viewModelLogin.login(email, secret)
    }

    private fun onRegister(register: Register){
        viewModelLogin.register(register)
    }

    private fun onForgot(email: String, secret: String){
        viewModelLogin.forgot(email, secret)
    }

    private fun changeActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding.profileImage.setImageURI(imageUri)
        }
    }
}