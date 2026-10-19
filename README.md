# AndroidProject
Создание приложения, которое позволит контролировать работу участников проекта.

## Сборка

Секреты (Firebase, keystore) не лежат в репозитории — они в Doppler.
Подробный гайд: [docs/SECRETS.md](docs/SECRETS.md).

```bash
brew install dopplerhq/cli/doppler
doppler login && doppler setup     # project: coflow, config: dev
make secrets                       # разложить файлы для Android Studio
make build                         # сборка
```
