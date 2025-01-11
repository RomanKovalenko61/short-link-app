## Задача

Разработать сервис сокращения ссылок с возможностью управления лимитом переходов и времени жизни ссылки.

## Конфигурация проекта

В каталоге config имеется файл app.properties. Значения установленные в нем влияют на работу приложения.

**prefix**=clck.ru/ строка, которая будет стоять перед сгенерированной последовательностью для короткой ссылки  
**generate-length**=6 число, которое устанавливает количество символов, для генерации короткой ссылки.   
**lifetime**=86400 время жизни ссылки в секундах  
**transition-count**=5 лимит количества переходов по ссылке

Также в этот каталог сохраняется дамп базы юзеров и коротких ссылок созданных в сеансе работы приложения.
Файлы будут созданы или обновлены при завершении сеанса работы приложения. Формат хранения данных json.
Используется библиотека Gson от Google

## Справка по работе с приложением

### Список команд

**login** uuid - зарегистрироваться в системе, если у вас есть uuid  
**logout** - выйти из системы, текущий uuid будет установлен в null  
**create** link lifetime - создать короткую ссылку и при желании установить время ее жизни  
**go** shortLink - переход по короткой ссылке  
**update** shortLink transitionLimit - установить лимит переходов по короткой ссылке     
**delete** shortLink - удалить короткую ссылку  
**inspect** shortLink - просмотреть информацию о короткой ссылке  
**getlinks** - получить список ваших коротких ссылок. Нужно быть зарегистрированным в системе     
**getusers** - получить список всех uuid которые зарегистрированы в системе  
**exit** - выйти из приложения

### Быстрый старт для работы с приложением.

Для работы приложения необходимо иметь uuid. Если у вас его нет, то его можно получить создав свою короткую ссылку.
Для этого введите команду **< create https://habr.ru >** . Параметр времени жизни ссылки является необязательным, его
можно не
вводить и он будет установлен в соответствие с конфигурационным файлом. Если вы хотите ввести свое значение, то
используете
команду **< create https://habr.ru 3600 >**. В этом случае система сравнит введенное вами время жизни ( 3600 секунд) и
выберет минимальное
между введенным и указанным в конфигурационном файле. На данный момент корректировка времени жизни созданной ссылки не
предусмотрена.
После создания короткой ссылки вы можете увидеть присвоенный вам uuid. Сохраните его отдельно. Он понадобится чтобы была
возможность переходить по короткой ссылке, редактировать или удалять ее. Без соответствующего uuid вы не сможете
произвести данные действия.

Теперь вы можете просмотреть список созданных вами коротких ссылок с помощью команды < **getlinks** >
Для просмотра информации о конкретной короткой ссылки введите команду < **inspect** [короткая ссылка] >. Вы можете
увидеть на какой
сайт ведет короткая ссылка, время ее жизни, сколько было совершенно переходов и какой задан лимит переходов.

Для перехода по короткой ссылке используете < **go** [короткая ссылка] >. Если время жизни еще позволяет и не исчерпан
лимит
переходов, то в окне браузера откроется сайт. При не соблюдении условий вы получите соответствующее уведомление.
Пример: < go clck.ru/TFgiQI >  
При этом, если время жизни истекло, то при попытке перехода по этой ссылке она будет удалена из системы. Лимит переходов
рассчитывается как
максимальное значение из указанного в конфигурационном файле и установленного для конкретной ссылки.

Пока время жизни короткой ссылки не истекло, вы можете редактировать лимит переходов командой < **
update** [короткая ссылка] [новый лимит переходов] >. Пример: < update clck.ru/TFgiQI 10 >
Влияние лимита переходов, см. команду < **go** > выше.

Удалить ссылку самостоятельно можно использовав команду < **delete** [короткая ссылка] >. Пример: < delete
clck.ru/TFgiQI >

Чтобы разлогинится в системе, используйте команду < **logout** >

Для проверки работоспособности можно получить весь список пользователей приложения командой < **getusers** >. Логинится
под ними
и действовать от их имени.





