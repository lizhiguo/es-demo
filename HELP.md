# ELASTICSEARCH
### 查询
> 检索和过滤,检索标题有"好人”,并且id大于0
```aidl
GET /goods/_search
{
  "query": {
    "bool": {
      "must": {
        "match": {
          "title": {
            "query": "好人"
          }
        }
      },
      "filter": {
        "range": {
          "id": {
            "gte": 0
          }
        }
      }
    }
  }
}
```
>检索标题中有"好人"或者"中国人"的文字
```aidl
GET /goods/_search
{
  "query": {
    "match": {
      "title": {
        "query": "好人 中国人"
      }
    }
  }
}
```
>and查询,检索remake中有"中国"和"起来"，并且title有”好人“
```aidl
GET /goods/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "remake": {
              "query": "中国"
            }
          }
        },{
          "match": {
            "remake": {
              "query": "站起"
            }
          }
        },{
          "match": {
            "title": {
              "query": "好人"
            }
          }
        }
      ]
    }
  }
}
```
#### 查询对照
1. match(全文搜索)
2. match_phrase:短语搜索(完全匹配)
3. 

