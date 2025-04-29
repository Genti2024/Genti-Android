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
- `Test` JUnit5, MockK
- `Third Party` Kakao Open API, Firebase Cloud Messaging, Google Play Billing API, AWS S3
- `Permission` Camera, External Storage, Notification, Billing

<br>

## ACHIEVEMENT

- 2024 정주영 창업경진대회 (아산나눔재단) 사업실행팀 선발 및 본상 수상
- 2024 고려대 KU 창업동아리 아이디어 트랙 선정
- 2024 동국대 아이디어 사업화 지원사업 선발
- 2024 KUCT 딥테크 스타트업 프론티어 선발전 도전상 수상
- 2024 K-Digital Challenge AI 스타트업 창업 경진대회 장려상 수상

<br>

## EXPERIENCE

- **MVI + Compose 전환** _ 기존 MVVM + XML (dataBinding) 구조에서 MVI + Jetpack Compose로 전환
- **CI/CD 자동화** _ Github Actions와 Firebase App Distribution을 연계하여 자동 앱 배포 프로세스 구축
- **모듈 간 의존성 관리 개선** _ 기존 buildSrc를 build-logic으로 전환하고, 버전 카탈로그 및 feature 모듈 분리 도입
- **클라우드 이미지 업로드** _ AWS S3 Presigned URL을 활용해 기기의 저장공간 내 이미지 클라우드 업로드 기능 구현
- **비동기 업로드 최적화** _ async와 await을 활용한 병렬 비동기 처리를 도입하여 여러 사진을 동시에 업로드
- **사진 촬영 및 캐시 업로드** _ FileProvider와 cacheDirectory를 활용해 카메라로 촬영한 사진을 저장 없이 즉시 업로드
- **인앱 결제 구현** _ Google Play 결제 라이브러리 v7을 활용해 안정적인 인앱 결제 시스템을 구축
- **푸시 알림 구현** _ Firebase Cloud Messaging을 통해 사용자가 사진 생성 완료 시 실시간 알림을 받을 수 있도록 구현
- **단위 테스트 구현** _ JUnit5와 MockK를 사용한 단위 테스트 구현을 통해 뷰모델 로직의 안정성 보장

<br>

## MODULE & PACKAGE CONVENTION

![project dot](https://github.com/user-attachments/assets/c776a94b-f4cf-45bb-8b73-d02a6d67f4eb)

![diagram](https://github.com/user-attachments/assets/4bcdd031-6d12-405d-8329-6511c3fad2a8)

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

## PRODUCT

  | 로그인 | 회원가입  | 온보딩 |
  | :-------------: | :-------------: | :-------------: |
  |  <img src="https://github.com/user-attachments/assets/b1f42fc3-463c-4101-90fb-5acd65081614" width=240 /> | <img src="https://github.com/user-attachments/assets/4470775b-a7cd-447f-b333-b4840398aa5a" width=240 /> |<img src="https://github.com/user-attachments/assets/0d0a556c-675c-4f78-85a8-c2e1cb5ff56d" width=240 /> |

  | 피드 | 프로필 | 본인인증 (초기 생성 시) |
  | :-------------: | :-------------: | :-------------: |
  |  <img src="https://github.com/user-attachments/assets/2601399d-3c57-446e-97ec-5f4450cf28a7" width=240 /> | <img src="https://github.com/user-attachments/assets/d1a0349f-8579-46d6-9820-5a363a3943b7" width=240 /> |<img src="https://github.com/user-attachments/assets/a6e32f6b-d73a-4135-8f2f-c20d524ec517" width=240 /> |

  | 생성 진입  | 생성1 (프롬프트 선택) | 생성2 (비율 선택) |
  | :-------------: | :-------------: | :-------------: |
  |  <img src="https://github.com/user-attachments/assets/593a9b43-e6d2-4f27-b48d-7294484666c9" width=240 /> | <img src="https://github.com/user-attachments/assets/664409cb-df4d-4e60-9507-2a478b2eace8" width=240 /> |<img src="https://github.com/user-attachments/assets/fd5ff57d-62ba-4885-83b0-a4aa474b8269" width=240 /> |

  | 생성3 (사진 선택) | 생성3 (부모님 사진 선택) | 생성 대기 |
  | :-------------: | :-------------: |  :-------------: |
  |  <img src="https://github.com/user-attachments/assets/353fa4a6-523e-49b2-a7f0-06a1a9b808c2" width=240 /> |<img src="https://github.com/user-attachments/assets/ad145181-ed49-4d21-82b2-7036fe265460" width=240 /> | <img src="https://github.com/user-attachments/assets/8587e6e9-5ebb-4712-819a-35dd629796ec" width=240 /> |

  | 생성 완료 (푸시알림)  | 결과 별점 | 결과 오류 제보 |
  | :-------------: | :-------------: | :-------------: | 
  | <img src="https://github.com/user-attachments/assets/d03b4118-ae33-4cc8-b10d-7c3345b5ab1d" width=240 /> |<img src="https://github.com/user-attachments/assets/912d04f8-284c-4391-b684-ffb264dc782a" width=240 /> |<img src="https://github.com/user-attachments/assets/5300a60a-f8cf-4457-93a8-c2207ee2e8b0" width=240 /> |

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

