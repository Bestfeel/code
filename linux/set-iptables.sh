#!/bin/bash

#  coding "feel"
#  /etc/sysconfig/iptables
# ssh root@MachineB 'bash -s' < local_script.sh
# use puppet later

declare -A hosts

#   对以下机器 端口全部开放
hosts=(
  [feel]=127.0.0.1
)

#  本机需要对外开放的端口
declare -A  mports     
mports=(
  [website]=80
)
# 声明 主机名
declare  -x  localname
#  主机名
localname="cloud"

#  清除规则

iptables  -F
iptables  -t nat -L


iptables  -P INPUT DROP
iptables  -P FORWARD DROP
iptables  -P OUTPUT ACCEPT

#  22  为ssh 端口,防止自锁在系统中


iptables   -A INPUT -p tcp -m state --state NEW -m tcp --dport 2221 -j ACCEPT
iptables   -A INPUT -p tcp -m state --state NEW -m tcp --dport 22 -j ACCEPT

iptables   -A  INPUT -i lo -j ACCEPT

iptables   -A   INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT


if [ "$(hostname)" == "$localname" ]; then
  #iptables -t nat -A PREROUTING -i eth0 -p tcp --dport 443 -j DNAT --to-destination :5601
  echo "$localname"
  for p in "${!mports[@]}"
  do
   echo  "外网开放端口 ${mports[$p]}"
   iptables   -A INPUT -p tcp -m state --state NEW -m tcp --dport ${mports[$p]} -j ACCEPT
  done
fi


for h in "${!hosts[@]}"
do
  if [ "$(hostname)" != "$h" ]; then
    iptables -A INPUT -s ${hosts[$h]} -j ACCEPT
  fi
done



#iptables -A INPUT -p icmp --icmp-type 8 -i eth0 -j ACCEPT

#屏蔽了nmap探测的icmp回应,以下规则是针对我自己的vps 添加的
iptables -A INPUT -j REJECT --reject-with icmp-host-prohibited


#iptables -A INPUT -p tcp --dport 443 -j ACCEPT

/sbin/iptables-save

iptables -L -v


#   close

#iptables -P INPUT ACCEPT 

#iptables -P FORWARD ACCEPT 

#iptables -P OUTPUT ACCEPT 

#iptables -F 
#iptables -t nat -L
#





