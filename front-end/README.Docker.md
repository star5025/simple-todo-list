# 前端Docker部署说明

## 构建镜像

```bash
docker build -t simple-todo-list-frontend .
```

## 运行容器

```bash
docker run -p 5173:5173 simple-todo-list-frontend
```

## 环境变量

前端应用可以通过环境变量进行配置：

- `VITE_API_URL`: 后端API地址，默认为`http://localhost:8088`

## 构建参数

在构建时可以通过以下参数自定义：

- `API_URL`: API后端地址

## 生产环境部署

在生产环境中，建议使用nginx等反向代理服务器来提供静态文件服务，而不是使用内置的serve工具。

## Docker Compose

可以与后端服务一起使用docker-compose进行部署：

```yaml
version: '3'
services:
  frontend:
    build: ./front-end
    ports:
      - "5173:5173"
    depends_on:
      - backend
    environment:
      - VITE_API_URL=http://backend:8088
  
  backend:
    build: ./back-end
    ports:
      - "8088:8088"
    environment:
      - DB_HOST=db
    depends_on:
      - db
      
  db:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=root
      - MYSQL_DATABASE=simple_todo_list
    ports:
      - "3306:3306"
```