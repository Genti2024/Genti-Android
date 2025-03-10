package kr.genti.setting

sealed class SettingIntent {
    data object Init : SettingIntent()
    data object BackButtonClick : SettingIntent()
    data object TermButtonClick : SettingIntent()
    data object PrivacyButtonClick : SettingIntent()
    data object CompanyButtonClick : SettingIntent()
    data object QuestionButtonClick : SettingIntent()
    data object LogoutButtonClick : SettingIntent()
    data object QuitButtonClick : SettingIntent()
}