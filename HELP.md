# ELASTICSEARCH
### 查询
> 检索和过滤,检索标题有"好人”,并且id大于0
1. gt: 大于
2. gte: 大于等于
3. lt: 小于
4. lte: 小于等于
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

## 分词解析
1. IK提供了两个分词算法:ik_smart和ik_max_word
2. 其中ik_smart为最少切分,ik_max_word为最细粒度划分
#### 分词解析-smart
```aidl
GET /goods/_analyze
{
  "tokenizer": "ik_smart",
  "text": "但是当然欢迎拉取请求"
}
# 返回数据
{
  "tokens": [
    {
      "token": "但是",
      "start_offset": 0,
      "end_offset": 2,
      "type": "CN_WORD",
      "position": 0
    },
    {
      "token": "当然",
      "start_offset": 2,
      "end_offset": 4,
      "type": "CN_WORD",
      "position": 1
    },
    {
      "token": "欢迎",
      "start_offset": 4,
      "end_offset": 6,
      "type": "CN_WORD",
      "position": 2
    },
    {
      "token": "拉",
      "start_offset": 6,
      "end_offset": 7,
      "type": "CN_CHAR",
      "position": 3
    },
    {
      "token": "取",
      "start_offset": 7,
      "end_offset": 8,
      "type": "CN_CHAR",
      "position": 4
    },
    {
      "token": "请求",
      "start_offset": 8,
      "end_offset": 10,
      "type": "CN_WORD",
      "position": 5
    }
  ]
}
```
#### 分词解析-ik_max_word
```aidl
GET /goods/_analyze
{
  "tokenizer": "ik_max_word",
  "text": "但是当然欢迎拉取请求"
}
# 返回
{
  "tokens": [
    {
      "token": "但是",
      "start_offset": 0,
      "end_offset": 2,
      "type": "CN_WORD",
      "position": 0
    },
    {
      "token": "当然",
      "start_offset": 2,
      "end_offset": 4,
      "type": "CN_WORD",
      "position": 1
    },
    {
      "token": "欢迎",
      "start_offset": 4,
      "end_offset": 6,
      "type": "CN_WORD",
      "position": 2
    },
    {
      "token": "拉",
      "start_offset": 6,
      "end_offset": 7,
      "type": "CN_CHAR",
      "position": 3
    },
    {
      "token": "取",
      "start_offset": 7,
      "end_offset": 8,
      "type": "CN_CHAR",
      "position": 4
    },
    {
      "token": "请求",
      "start_offset": 8,
      "end_offset": 10,
      "type": "CN_WORD",
      "position": 5
    }
  ]
}
```
#### 分词解析-standard
```aidl
GET /goods/_analyze
{
  "tokenizer": "standard",
  "text": "但是当然欢迎拉取请求"
}
# 返回
{
  "tokens": [
    {
      "token": "但",
      "start_offset": 0,
      "end_offset": 1,
      "type": "<IDEOGRAPHIC>",
      "position": 0
    },
    {
      "token": "是",
      "start_offset": 1,
      "end_offset": 2,
      "type": "<IDEOGRAPHIC>",
      "position": 1
    },
    {
      "token": "当",
      "start_offset": 2,
      "end_offset": 3,
      "type": "<IDEOGRAPHIC>",
      "position": 2
    },
    {
      "token": "然",
      "start_offset": 3,
      "end_offset": 4,
      "type": "<IDEOGRAPHIC>",
      "position": 3
    },
    {
      "token": "欢",
      "start_offset": 4,
      "end_offset": 5,
      "type": "<IDEOGRAPHIC>",
      "position": 4
    },
    {
      "token": "迎",
      "start_offset": 5,
      "end_offset": 6,
      "type": "<IDEOGRAPHIC>",
      "position": 5
    },
    {
      "token": "拉",
      "start_offset": 6,
      "end_offset": 7,
      "type": "<IDEOGRAPHIC>",
      "position": 6
    },
    {
      "token": "取",
      "start_offset": 7,
      "end_offset": 8,
      "type": "<IDEOGRAPHIC>",
      "position": 7
    },
    {
      "token": "请",
      "start_offset": 8,
      "end_offset": 9,
      "type": "<IDEOGRAPHIC>",
      "position": 8
    },
    {
      "token": "求",
      "start_offset": 9,
      "end_offset": 10,
      "type": "<IDEOGRAPHIC>",
      "position": 9
    }
  ]
}
```
## 查询

### term 查询
参考 https://www.elastic.co/guide/cn/elasticsearch/guide/current/_most_important_queries.html
>term 查询被用于精确值匹配，这些精确值可能是数字、时间、布尔或者那些 not_analyzed 的字符串：
