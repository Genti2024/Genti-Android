package kr.genti.presentation.result.openchat

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

@HiltViewModel
class OpenchatViewModel
    @Inject
    constructor(
        private val generateRepository: GenerateRepository,
    ) : ViewModel()
