# Generate CSV

Предельно простая программа, работающая с csv файлами:
1. Берёт первую линию в csv
2. Копирует её столько раз, сколько Вам нужно
3. Имя и имя в источнике должны быть уникальны, поэтому для них генерируется через автоинкремент новые значения. Например:
```csv
-- пример результата работы приложения
test-data-simple-1-1; ... ; source-1-1; ...
test-data-simple-1-2; ... ; source-1-2; ...
test-data-simple-1-3; ... ; source-1-3; ...
test-data-simple-1-N; ... ; source-1-N; ...
```
4. Остальные строки остаются без изменений

<br>

> Если программа Вам полезна, могу её доработать под общий случай и сделать её полноценной библиотекой. 😇

<br>

## Как запустить 

Написал два гайда снизу. Рекомендую запускать через Docker - тупо проще 🤷.

### Docker

1. Поместите свой csv файл в папку `data/input`
2. Задайте переменнные в docker-compose:
```yaml
environment:
   # откуда берутся данные (путь до вашего csv файла)
   INPUT_CSV_FILE: "data/in/in-simple-5-swingdoor.csv"
   # куда сохраняем результат
   OUTPUT_CSV_FILE: "data/out/test-data-simple-5-swingdoor.csv"
   # сколько раз повторяем строку
   REPEAT_CSV_LINE_TIMES: "50000"
```
3. Запустите docker-compose
4. Найдите результат выполнения в директории `data/output`


<br>

### Native java

1. Поместите свой csv файл в папку `data/input`
2. Задайте переменные:
```bash
# откуда берутся данные (путь до вашего csv файла)
export INPUT_CSV_FILE="data/in/in-simple-5-swingdoor.csv"
   
# куда сохраняем результат
export OUTPUT_CSV_FILE="data/out/test-data-simple-5-swingdoor.csv"

# сколько раз повторяем строку
export REPEAT_CSV_LINE_TIMES="50000"
```

3. Установите maven и java (версии 11 или выше):
```bash
# установка для ubuntu
sudo apt-get update
sudo apt-get install maven 
sudo apt-get install openjdk-11-jdk
```

4. Соберите билд:
```bash
mvn package spring-boot:repackage -DskipTests
```

5. Запустите программу:
```bash
java -jar target/GenerateCSV-1.0-SNAPSHOT.jar
```
