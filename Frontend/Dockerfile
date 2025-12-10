FROM node:20-alpine AS build

LABEL authors="l7aur"

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

WORKDIR /DS2025_30441_Grad_Laurentiu_Calin_1_Frontend

COPY ./package*.json .

RUN npm install

COPY . .

RUN npm run build

FROM nginx:alpine

COPY --from=build /DS2025_30441_Grad_Laurentiu_Calin_1_Frontend/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
