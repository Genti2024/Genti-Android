<p align="left"><img src="https://github.com/user-attachments/assets/15e61bb6-dfdc-4d56-a7ed-af05f21144b8" height=110></p>

# Genti
```
내 마음대로 표현하는, 하나뿐인 AI 사진 제작 서비스
```

<br>

## TEAM
![2024-08-01_00-28-32](https://github.com/user-attachments/assets/64bb2fa3-cd67-430a-ae53-ff20c76d53b7)

### Android Contributor

[![contributors](https://contrib.rocks/image?repo=Genti2024/Genti-Android)](https://github.com/Genti2024/Genti-Android/contributors)

<br>

## MODULE & PACKAGE CONVENTION

![project dot](https://github.com/user-attachments/assets/c776a94b-f4cf-45bb-8b73-d02a6d67f4eb)

```
🗃️app

🗃️build-logic

🗃️core
 ┣ 🗃️common
 ┣ 🗃️datastore
 ┣ 🗃️designsystem
 ┣ 🗃️navigation
 ┗ 🗃️network

🗃️data
 ┣ 📂di
 ┣ 📂dto
 ┣ 📂datasource
 ┣ 📂datasourceImpl
 ┣ 📂repositoryImpl
 ┗ 📂service

🗃️domain
 ┣ 📂entity
 ┗ 📂repository

🗃️feature
 ┣ 🗃️feed
 ┣ 🗃️generate
 ┣ 🗃️main
 ┣ 🗃️onboarding
 ┣ 🗃️profile
 ┣ 🗃️result
 ┗ 🗃️setting

```

<br>

## TECH STACK
- `Version`  Kotlin 2.0.0 / JVM 11 / SDK 28~35 / AGP 8.8.0
- `Architecture`  Clean Architecture, MVI, Multi-Module
- `UI`  Jetpack Compose
- `DI`  Hilt
- `Network`  Retrofit2, OkHttp
- `Async`  Coroutine, Flow
- `Build Tools`  Gradle Version Catalog + Custom Convention Plugins
- `CI/CD`  Github Actions (to Firebase App Distribution)
- `Analytics` Amplitude
- `Third Party` Kakao Open API, AWS S3 with Presigned Url
- `Permission` Camera, External Storage(PhotoPicker), Notification(Firebase Cloud Messaging), Billing(Google Play Billing)

<br>

## ACHIEVEMENT

- 2024 정주영 창업경진대회 (아산나눔재단) 사업실행팀 선발 및 본상 수상
- 2024 고려대 KU 창업동아리 아이디어 트랙 선정
- 2024 동국대 아이디어 사업화 지원사업 선발
- 2024 KUCT 딥테크 스타트업 프론티어 선발전 도전상 수상
- 2024 K-Digital Challenge AI 스타트업 창업 경진대회 장려상 수상

<br>

## EXPERIENCE

- **MVI 전환** -- 기존 MVVM + XML (dataBinding, viewBinding) 구조를 MVI + Jetpack Compose로 전환하여 로직의 효율성과 유지보수성을 향상
- **의존성 관리 혁신** -- 기존 buildSrc를 build-logic으로 전환하고 버전 카탈로그 및 feature 모듈 분리를 도입해 의존성 관리의 효율성과 가독성을 개선
- **인앱 결제 구현** -- Google Play 결제 라이브러리 v7을 활용해 안정적인 인앱 결제 시스템을 구축
- **CI/CD 자동화** -- Github Actions와 Firebase App Distribution을 연계하여 자동 앱 배포 프로세스를 구축
- **클라우드 이미지 업로드** -- AWS S3 Presigned URL을 활용해 클라우드로 이미지 업로드 기능을 구현
- **실시간 사진 업로드** -- FileProvider와 cacheDirectory를 활용해 카메라로 촬영한 사진을 저장 없이 즉시 업로드할 수 있도록 설계
- **유저 사진 선택 기능** -- 버전 분기 처리를 통해 PhotoPicker와 기존 갤러리 파일 탐색기를 모두 지원, 유저가 손쉽게 사진을 선택할 수 있도록 구현
- **비동기 업로드 최적화** -- async와 await을 활용한 병렬 비동기 처리를 도입하여 여러 사진 업로드 시간을 효과적으로 단축

<br>

## PRODUCT
![Group 8245](https://github.com/user-attachments/assets/6616126f-8be8-4011-afc8-d312145cd5e9)

<br>

## SOLUTIONS
![Group 8250](https://github.com/user-attachments/assets/c96de744-a45f-4512-9896-cd4c6b278ecb)
![Group 8251](https://github.com/user-attachments/assets/45c89b6e-6c74-498d-b498-aaa1bad0484f)
![Group 8252](https://github.com/user-attachments/assets/27210a73-d53c-479c-be24-bc9fe4c5a060)

<br>

## PROGRESS
![Group 8249](https://github.com/user-attachments/assets/30ab4785-68f0-4490-8bbe-a2b0a9a269e6)
![Group 8248](https://github.com/user-attachments/assets/a78918f8-14e5-4853-b0f2-806e880d086a)
![Group 8247](https://github.com/user-attachments/assets/cf32392b-c7f1-4966-9439-ffe150bdc4d0)

<br>

