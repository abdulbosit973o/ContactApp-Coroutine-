package com.example.contactapp.presentation.screens.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.contactapp.R
import com.example.contactapp.data.local.sharedPref.SharedPreferences
import com.example.contactapp.data.model.ContactUIData
import com.example.contactapp.data.remote.request.ContactAddData
import com.example.contactapp.data.remote.request.ContactEditData
import com.example.contactapp.databinding.FragmentHomeBinding
import com.example.contactapp.presentation.adapters.HomeContactAdapter
import com.example.contactapp.presentation.screens.viewModules.HomeViewModel
import com.example.contactapp.presentation.screens.viewModules.SignUpViewModel
import com.example.contactapp.presentation.screens.viewModules.impl.HomeViewModelImpl
import com.example.contactapp.presentation.screens.viewModules.impl.SignUpViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreen : Fragment(R.layout.fragment_home) {
    private lateinit var dialog: Dialog
    private val viewModel: HomeViewModel by viewModels<HomeViewModelImpl>()
    private val shared = SharedPreferences
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val adapter by lazy { HomeContactAdapter()}



    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadContacts()
    }
    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        initActions()

        viewModel.listFromServer.observe(this, listObserver)
        viewModel.emptyListLiveData.observe(this, emptyListObserver)
        viewModel.messageLiveData.observe(this, messageObserver)
        viewModel.refreshLiveData.observe(this, refreshObserver)
    }

    private val listObserver = Observer<List<ContactUIData>> {
        Toast.makeText(requireContext(), it.size.toString(), Toast.LENGTH_SHORT).show()
        adapter.submitList(it)
    }
    private val messageObserver = Observer<String> {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }
    private val emptyListObserver = Observer<Boolean> {
        if (it) binding.placeholder.visibility = View.VISIBLE else binding.placeholder.visibility = View.GONE
    }
    private val refreshObserver = Observer<Boolean> {
        binding.swipeRefresh.isRefreshing = it
    }

    private fun openLogOutDialog() {
        dialog.setContentView(R.layout.dialog_restart)

        dialog.findViewById<CardView>(R.id.yes).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<CardView>(R.id.no).setOnClickListener {
            navController.navigate(HomeScreenDirections.actionHomeScreenToSignIn())
            shared.userIsLogOut(true)
            dialog.dismiss()
        }
        dialog.findViewById<ImageView>(R.id.cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun showBottomSheetDialog(data: ContactUIData) {
        dialog.setContentView(R.layout.dialog_edit_delete)

        dialog.findViewById<ImageView>(R.id.delete).setOnClickListener {
            viewModel.deleteContact(data)
            dialog.dismiss()
        }
        dialog.findViewById<ImageView>(R.id.edit).setOnClickListener {
            showEditDialog(data)
        }



        dialog.show()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun showEditDialog(data: ContactUIData) {
        dialog.setContentView(R.layout.dialog_exam)

        val firstName = dialog.findViewById<AppCompatEditText>(R.id.firstName)
        val lastName = dialog.findViewById<AppCompatEditText>(R.id.lastName)
        val phoneNumber = dialog.findViewById<AppCompatEditText>(R.id.phoneNumberAdd)

        firstName.setText(data.firstName)
        lastName.setText(data.lastName)
        phoneNumber.setText(data.phone)

        dialog.findViewById<AppCompatButton>(R.id.saveBtn).setOnClickListener {
            if (phoneNumber.text.toString().startsWith("+998") && firstName.text.toString().length > 3 &&  lastName.text.toString().length > 3) {
                viewModel.editContact(ContactEditData(data.id, firstName.text.toString(), lastName.text.toString(), phoneNumber.text.toString()))
                dialog.dismiss()
            }
            else Toast.makeText(requireContext(), "Malumotlar xato kiritildi", Toast.LENGTH_SHORT).show()

        }


        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.CENTER)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun openAddDialog() {
        dialog.setContentView(R.layout.dialog_exam)


        dialog.findViewById<AppCompatButton>(R.id.saveBtn).setOnClickListener {
            if (dialog.findViewById<AppCompatEditText>(R.id.phoneNumberAdd).text.toString().startsWith("+998") &&
                dialog.findViewById<AppCompatEditText>(R.id.firstName).text.toString().length > 3 &&
                dialog.findViewById<AppCompatEditText>(R.id.lastName).text.toString().length > 3) {

                viewModel.addContact(
                    ContactAddData(
                        dialog.findViewById<AppCompatEditText>(R.id.firstName).text.toString(),
                        dialog.findViewById<AppCompatEditText>(R.id.lastName).text.toString(),
                        dialog.findViewById<AppCompatEditText>(R.id.phoneNumberAdd).text.toString()
                    )
                )
                dialog.dismiss()

            }
            else Toast.makeText(requireContext(), "Malumotlar xato kiritildi", Toast.LENGTH_SHORT).show()
        }


        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.CENTER)
    }
    private fun initActions(){
        binding.addButton.setOnClickListener {
            openAddDialog()
        }
        binding.logOut.setOnClickListener {
            openLogOutDialog()
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadContacts()
        }
        adapter.itemTouchListener = {
            navController.navigate(HomeScreenDirections.actionHomeScreenToInfoScreen())
        }
        adapter.itemLongTouchListener = {
            showBottomSheetDialog(it)
        }
    }
}