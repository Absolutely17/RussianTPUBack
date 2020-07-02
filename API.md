#API

* GET /menu?language={language}

Response:
````javascript
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
