package com.example.fe.myapplication.model

data class ProjectResponse(
    val projects: List<Project>,   // 从服务器获取的项目列表
    val total_pages: Int           // 总页数
)

// {
//   "projects": [
//     {
//       "id": 1,
//       "name": "项目1",
//       "location": "场地1",
//       "status": "已完成"
//     },
//     {
//       "id": 2,
//       "name": "项目2",
//       "location": "场地2",
//       "status": "进行中"
//     }
//   ],
//   "total_pages": 5
// }