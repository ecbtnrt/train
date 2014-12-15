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


https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=passenger&rand=randp&0.9170868629589677

https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn
randCode:vkks
rand:randp
_json_att:
REPEAT_SUBMIT_TOKEN:489915458494510c9343d9e787bd53d0

https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo
cancel_flag:2
bed_level_order_num:000000000000000000000000000000
passengerTicketStr:3,0,1,xx,1,xxxx,mmmm,N
oldPassengerStr:xx,1,xxxx,1_
tour_flag:dc
randCode:vkks
NjA2MTYz:YjUwZTFhZDA5OGMwOTRjOA==
_json_att:
REPEAT_SUBMIT_TOKEN:489915458494510c9343d9e787bd53d0

https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue
passengerTicketStr:3,0,1,xx,1,dd,mmm,N
oldPassengerStr:xx,1,dd,1_
randCode:vkks
purpose_codes:00
key_check_isChange:26D1EFABE4229C44684002297B53E689954F114B9581B1F599DEB7A6
leftTicketStr:40415500763026350574
train_location:H3
_json_att:
REPEAT_SUBMIT_TOKEN:489915458494510c9343d9e787bd53d0

https://kyfw.12306.cn/otn/confirmPassenger/resultOrderForDcQueue
orderSequence_no:oorderno
_json_att:
REPEAT_SUBMIT_TOKEN:489915458494510c9343d9e787bd53d0



ticket
