package com.aking.syncchord.home.ui.workspace

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.aking.reactive.base.BaseFragment
import com.aking.reactive.base.BaseViewModel
import com.aking.reactive.base.Intent
import com.aking.reactive.base.Reactive
import com.aking.reactive.base.Reducer
import com.aking.reactive.base.StateDiff
import com.aking.reactive.widget.logD
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentCreateWorkspaceBinding
import com.aking.syncchord.home.data.repository.WorkspaceRepository
import com.aking.syncchord.util.SnackBarHelper
import com.google.android.material.internal.ViewUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

data class WorkspaceInputState(
    val workspaceName: String = "",
    val complete: Boolean = false,
    var msg: String? = null,
    var error: Throwable? = null
)

sealed class Action : Intent {
    data class CreateWorkspace(val name: String) : Action()
    data object UserMessageShown : Action()
    data object ErrorShown : Action()
}

class CreateWorkspaceViewModel(private val workspaceRepo: WorkspaceRepository) :
    BaseViewModel<WorkspaceInputState>(WorkspaceInputState()), Reducer<Action> {
    override fun onInitialize() {}
    override fun reducer(intent: Action) {
        when (intent) {
            is Action.CreateWorkspace -> createWorkspace(intent.name)
            is Action.UserMessageShown -> update { copy(msg = null) }
            is Action.ErrorShown -> update { copy(error = null) }
        }
    }

    /** 创建workspace */
    private fun createWorkspace(name: String) = viewModelScope.launch {
        logD("createWorkspace")
        if (name.isBlank()) {
            update { copy(msg = "服务器名称不能为空") }
            return@launch
        }
        workspaceRepo.createWorkspace(name).onSuccess {
            logD("onSuccess $it")
            update { copy(msg = "创建成功") }
            delay(1000)
            update { copy(complete = true) }
        }.onFailure {
            logD("onFailure $it")
            update { copy(error = it) }
        }

    }
}

/**
 * @author Created by Ak on 2025-01-02 21:50.
 */
class CreateWorkspaceFragment :
    BaseFragment<FragmentCreateWorkspaceBinding>(R.layout.fragment_create_workspace),
    Reactive<WorkspaceInputState> {

    private val viewModel: CreateWorkspaceViewModel by viewModel()

    override fun FragmentCreateWorkspaceBinding.initView() {
        toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        // hide keyboard
        etServiceName.setOnFocusChangeListener { v, hasFocus ->
            @SuppressLint("RestrictedApi")
            if (hasFocus.not()) ViewUtils.hideKeyboard(v)
        }
        btnCreateService.setOnClickListener {
            viewModel.reducer(Action.CreateWorkspace(etServiceName.text.toString()))
        }
    }

    override fun initData() {
        viewModel.initialize(this)
    }

    override suspend fun render(state: WorkspaceInputState, diff: StateDiff<WorkspaceInputState>) {
        binding.btnCreateService.isEnabled = state.workspaceName.isNotEmpty()
        if (state.complete) {
            requireActivity().supportFragmentManager.popBackStack()
        }
        state.msg?.let {
            showMsg(it)
        }
        state.error?.let {
            showError(it)
        }
    }

    private fun showMsg(msg: String) {
        SnackBarHelper.showMessage(msg, binding.root)
        viewModel.reducer(Action.UserMessageShown)
    }

    private fun showError(error: Throwable) {
        SnackBarHelper.showError(error.message ?: "创建失败", error, binding.root)
        viewModel.reducer(Action.ErrorShown)
    }

    companion object {
        fun newInstance() = CreateWorkspaceFragment()
    }
}