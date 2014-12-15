train
=====
ticket query: 
init:
1. https://kyfw.12306.cn/otn/leftTicket/init

2. method get with dynamic js

query:
log 
https://kyfw.12306.cn/otn/leftTicket/log?leftTicketDTO.train_date=2015-01-28&leftTicketDTO.from_station=SHH&leftTicketDTO.to_station=WHN&purpose_codes=ADULT

1. https://kyfw.12306.cn/otn/leftTicket/queryT?leftTicketDTO.train_date=2015-01-28&leftTicketDTO.from_station=SHH&leftTicketDTO.to_station=WHN&purpose_codes=ADULT

query parameters:
leftTicketDTO.train_date:2015-01-28
leftTicketDTO.from_station:SHH
leftTicketDTO.to_station:WHN
purpose_codes:ADULT

response:
json object,  work with jsonobj.data --> ticket number,type,etc

order
1. check user
https://kyfw.12306.cn/otn/login/checkUser
_json_att:

2. submit order request
https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest

ODc5NTU0:NzY3MDg3MzdhY2Y4N2Y4Yw==
myversion:undefined
secretStr:MjAxNS0wMS0yOCMwMCNaMjcjMTA6MTQjMjE6MTgjNTUwMDAwMFoyNzAyI1NOSCNXQ04jMDc6MzIj5LiK5rW35Y2XI+atpuaYjCMwMSMwNiM0MDQxNTUwMDc2MzAyNjM1MDU3NSNIMyMxNDE4NjI0OTc3MTA4I0VFMkI2OThCMjdBNTZCQjI0Qzg5NkRFQzQ1QkNCQkIyQkE0RTBCM0QxMDcxNEQ2MUFGMUFDQzk5
train_date:2015-01-28
back_train_date:2014-12-15
tour_flag:dc
purpose_codes:ADULT
query_from_station_name:上海
query_to_station_name:武汉
undefined:


https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=passenger&rand=randp&






ticket
