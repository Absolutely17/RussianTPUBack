#API

* GET /menu?language={language}

Response:
````javascript
[
  {
    "name": "Учеба",
    "level": "1",
    "language": "русский",
    "sublevels": [
      {
        "name": "Расписание",
        "level": 2,
        "language": "русский",
        "position": 1,
        "parentLanguage": "русский"
      }
    ]
  },
  {
    "name": "Новости",
    "level": 1,
    "language": "русский",
    "sublevels": [
      {
        "name": "ТПУ",
        "level": 2,
        "language": "русский",
        "position": 1,
        "parentLanguage": "русский"
      }
    ]
  }
]
````
