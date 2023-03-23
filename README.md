# Generate CSV

Простая, как пробка, программа, которая дублирует первую строку с данными в CSV файлами.

Чтоб воспользоваться:
1. Скачать свежий образ (можно и отсюда собрать):
```
docker pull alimkugot/generate-csv:latest # docker build -t generate-csv .  
```

2. Запустить dockerfile с переменными:
   - `CSV_FILE_PATH` - путь до csv файла (помещайте свой csv файл в директорию data/)
   - `REPEAT_FIRST_LINE_TIMES` сколько раз повторится первая строка в указанном csv файле (по дефолту равно 1)
```
docker run generate-csv -e CSV_FILE_PATH=data/example.csv -e REPEAT_FIRST_LINE_TIMES=1000
```
