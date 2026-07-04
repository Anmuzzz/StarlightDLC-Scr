# IRC Server Hosting Guide

## Требования
- Java 21+
- Открытый порт (по умолчанию 20036)

## Быстрый старт

### Способ 1: Скомпилировать из исходников
```bash
# В директории с сервером
./gradlew build
java -jar build/libs/irc-server.jar
```

### Способ 2: Запуск готового jar
```bash
java -jar irc-server.jar [порт]
```

По умолчанию порт 20036, макс. 100 клиентов.

### Запуск в фоне (Linux)
```bash
nohup java -jar irc-server.jar > irc.log 2>&1 &
```

### Запуск как systemd сервис
```ini
# /etc/systemd/system/irc-chat.service
[Unit]
Description=IRC Chat Server
After=network.target

[Service]
Type=simple
User=youruser
WorkingDirectory=/opt/irc-server
ExecStart=/usr/bin/java -jar /opt/irc-server/irc-server.jar
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
```

```bash
sudo systemctl enable irc-chat
sudo systemctl start irc-chat
```

## Использование в клиенте
1. Включить модуль `ClientUsers`
2. В настройках указать IP сервера, порт и никнейм
3. Откроется окно чата
4. Enter — отправить сообщение
5. Esc — закрыть

## Проверка работы
```bash
# Через telnet
telnet <ip> 20036
HELLO|TestUser
MSG|Привет всем!
```
