# DJ Simulator

[![Android APK](https://github.com/alessandrocdifalco/Dj_simulator/actions/workflows/android.yml/badge.svg)](https://github.com/alessandrocdifalco/Dj_simulator/actions/workflows/android.yml)

Simulatore di carriera da DJ nativo per Android, scritto in Kotlin e Jetpack Compose.

## Prima versione giocabile

- creazione del DJ e scelta della scena;
- ciclo settimanale di cinque giorni e serata finale;
- attività con costi e conseguenze: pratica, digging, produzione, networking, lavoro e riposo;
- statistiche di tecnica, selezione, produzione e reputazione;
- economia, energia, follower e storico dell'ultimo evento;
- simulazione della serata influenzata dalla preparazione;
- salvataggio locale automatico e reset della carriera;
- nessun account, cloud o servizio a pagamento.

## Build

Richiede JDK 17, Android SDK 35 e Gradle 8.9:

```bash
gradle :app:assembleDebug
```

L'APK viene scritto in `app/build/outputs/apk/debug/app-debug.apk`. Il workflow GitHub Actions allegato crea automaticamente l'artefatto `dj-simulator-debug-apk`.
