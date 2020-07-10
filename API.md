#API

* #####GET /menu?language={language}

Response:
````json
[
  {
    "id": "14663a1f-dfb3-468a-8f61-16fead1ac357",
    "name": "Учеба",
    "level": "1",
    "position": "1",
    "type": "LINKS_LIST",
    "childrens": [
      {
        "id": "0049c068-1d51-4c37-862f-19c96a6d100b",
        "name": "Расписание",
        "level": 2,
        "position": 1,
        "type": "LINK"
      },
      {
        "id": "64E8E7A7-B2DD-4B79-A24E-E8D021391754",
        "name": "Предметы",
        "level": 2,
        "position": 2,
        "type": "ARTICLE",
        "idArticle": "65917560-BC37-4F92-88A9-95F3E98CC5F8"
      }
    ]
  },
  {
    "id": "287647cf-12c1-4d6f-96ac-97524348e559",
    "name": "Новости",
    "level": 1,
    "position": "2",
    "type": "LINKS_LIST",
    "childrens": [
      {
        "id": "c3d5e555-43ee-4d32-a19a-245812bf5e77",
        "name": "ТПУ",
        "level": 2,
        "position": 1,
        "type": "FEED_LIST"
      }
    ]
  }
]
````

* #####GET /article/{id} - получение полной версии статьи

````json
{
   "id":"5346155C-E735-4A68-A63A-ECB8E9746526",
   "topic":"Тестовая статья",
   "text":"Здесь идет текст статьи. Его много, очень много.",
   "subject":"тест",
   "createDate":"14:58 06.07.2020"
}
````

* #####GET /article/list/{id}?fromMenu=true/false (defaultValue=false)
````json
{
   "count":2,
   "list":[
      {
         "id":"65917560-BC37-4F92-88A9-95F3E98CC5F8",
         "topic":"расписание",
         "briefText":"test",
         "subject":"расписание",
         "createDate":"14:58 06.07.2020"
      },
      {
         "id":"AADE21B2-6D18-4C66-A0C7-9A2B345FC4E8",
         "topic":"расписание",
         "briefText":"test",
         "subject":"расписание",
         "createDate":"14:58 06.07.2020"
      }
   ]
}
````

* #####GET /media/img/{id}
````json
/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxITEhUTExMWFhUXGSAbGBgYGB0fHxog
HiEdHR8fGh8dHSgiHx0lHh4aITEiJSkrLi4uHSIzODMtNygtLisBCgoKDg0OGxAQGzgm
ICYwLS0vMC8tLS0vLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0t
Lf/AABEIAN0A5AMBEQACEQEDEQH/xAAbAAACAwEBAQAAAAAAAAAAAAAFBgMEBwIBAP/E
AEQQAAIBAgMFBQQIBQIFBAMAAAECAwQRABIhBQYxQVETImFxgQcyQpEUI1JicqGxwTOC
ktHwQ+EVJFOi8RYXJcKys+L/xAAbAQACAwEBAQAAAAAAAAAAAAAFBgIDBAEAB//EADQR
AAEEAgICAgEDAwMEAQUAAAEAAgMEBRESIRMxIkEUIzJRBmGBFUJxJDORobFDUmJy0f/a
AAwDAQACEQMRAD8AfN4agBTyNtDjBSsQ2une0Xqs8bOlmW1N42QlCfLDN/p4fCWHv+Fa
94b2UGp98GU8TjLjcawu4lDbckbgmbZ2+kci9nLZlOmuGKTGCu3k1K9mOQHlGo6iUwtG
6Euik9mw4lfijPiALjrbw1HT228SD0VQOdhnF47Qqoo8tcLaqwup6g2IPyOAdzMEQEj6
TZj6YdVB/haxTTLDTi/MYA0siLMnB/2tFaqGu2k6p3t7N2XNpxGGSTGbgc369rc57Se0
Q2PvbHMMshBB/LC5WwTt8gEPtTuhPJir1MAilePjHMCUPLNYgj1Bv/Lhlr4wEhx9hTZm
hK3kfYVXau1Mmz4deK3OCtHFRukO0vtumSZxWf7L2ZJNJnINm0+eM91wpS9el2xYfL0r
dfu1LGzOtwRjThpo5C4uUWzv1xR7YW2TNSzwP7yqWA8VF8a7YhZMOPorHM+StOCPRV+f
bIihnm5yMAPIKv7nFcDY3yjfoLrDJPPv6CS12XNVEyNex4DFOaljj4lnpa3zvHxU2293
