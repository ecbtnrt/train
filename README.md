train
=====
ticket query: 
init:
1. https://kyfw.12306.cn/otn/leftTicket/init

2. method get with dynamic js

query:
1. https://kyfw.12306.cn/otn/leftTicket/queryT?leftTicketDTO.train_date=2015-01-28&leftTicketDTO.from_station=SHH&leftTicketDTO.to_station=WHN&purpose_codes=ADULT

query parameters:
leftTicketDTO.train_date:2015-01-28
leftTicketDTO.from_station:SHH
leftTicketDTO.to_station:WHN
purpose_codes:ADULT

response:
json object,  work with jsonobj.data --> ticket number,type,etc




ticket
