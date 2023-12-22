package com.tri.sulton.inigua.view.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tri.sulton.inigua.R
import com.tri.sulton.inigua.databinding.FragmentProfileBinding
import com.tri.sulton.inigua.view.ViewModelFactory
import com.tri.sulton.inigua.view.catalog.MainViewModel
import com.tri.sulton.inigua.view.welcome.WelcomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.token.isEmpty()) {
                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                activity?.finish()
            } else {
                binding.toolbar.title = user.username
            }
        }

        binding.toolbar.inflateMenu(R.menu.main_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.signout -> {
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle(getString(R.string.logout))
                        setMessage(getString(R.string.logout_message))
                        setPositiveButton(getString(R.string.yes)) { _, _ ->
                            viewModel.logout()
                        }
                        setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                            dialog.cancel()
                        }
                        create()
                        show()
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}