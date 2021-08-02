#!/usr/bin/env bash
docker stop newbeeplus
docker rm newbeeplus
docker rmi registry.cn-shanghai.aliyuncs.com/wayn111/newbee-mall-plus
docker pull registry.cn-shanghai.aliyuncs.com/wayn111/newbee-mall-plus
docker run -d -p 28079:28079 -v "${PWD}":/logs -v /opt/newbee-mall-plus/upload:/opt/newbee-mall-plus/upload --name newbeeplus registry.cn-shanghai.aliyuncs.com/wayn111/newbee-mall-plus
