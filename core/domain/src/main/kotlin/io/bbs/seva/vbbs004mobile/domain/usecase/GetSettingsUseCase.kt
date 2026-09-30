package io.bbs.seva.vbbs004mobile.domain.usecase

import io.bbs.seva.vbbs004mobile.domain.model.AppSettings
import io.bbs.seva.vbbs004mobile.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: AppSettingsRepository
) {
    operator fun invoke(): Flow<AppSettings> {
        return repository.appSettings
    }
}
