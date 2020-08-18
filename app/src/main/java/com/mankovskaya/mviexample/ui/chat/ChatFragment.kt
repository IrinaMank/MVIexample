package com.mankovskaya.mviexample.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mankovskaya.mviexample.R
import com.mankovskaya.mviexample.databinding.FragmentChatBinding
import com.mankovskaya.mviexample.model.base.BaseFragment
import com.mankovskaya.mviexample.model.feature.ChatAction
import com.mankovskaya.mviexample.model.feature.ChatState
import com.mankovskaya.mviexample.model.feature.ChatViewModel
import kotlinx.android.synthetic.main.item_message_field.view.*
import org.koin.android.viewmodel.ext.android.viewModel


class ChatFragment : BaseFragment<ChatViewModel>() {
    override val fragmentViewModel: ChatViewModel by viewModel()

    private val adapter by lazy { ChatAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentChatBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_chat, container, false
        )
        val view: View = binding.root
        with(binding) {
            lifecycleOwner = this@ChatFragment
            viewModel = fragmentViewModel
            stateViewModel = fragmentViewModel.stateViewModel
            val layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            chatRecyclerView.layoutManager = layoutManager
            chatRecyclerView.adapter = adapter
            chatRecyclerView.itemAnimator = DefaultItemAnimator()
            fragmentViewModel.getStateRelay().observe(this@ChatFragment as LifecycleOwner,
                Observer<ChatState> {
                    adapter.setItems(it.messages, true)
                    if (it.shouldScrollToEnd) {
                        layoutManager.scrollToPosition(adapter.itemCount - 1)
                    }
                })
            fieldLayout.sendButton.setOnClickListener {
                fragmentViewModel.reactOnAction(
                    ChatAction.MessageSent(
                        fieldLayout.fieldEditText.text?.toString() ?: ""
                    )
                )
                fieldLayout.fieldEditText.setText("")
            }
        }
        return view
    }

}