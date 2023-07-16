.text
.globl main

# --------------------------- DIAVASE TON PINAKA --------------------------------------

main:		
	la $t1,pin		# $t1 is base register of pin 	
	li $t2,0		# $t2 is plithos akeraiwn = 0		
	li $v0,5		# diavase akeraio apo to xrhsth
	syscall
		
while1:	
	beqz $v0,exit1		# if (akeraios == 0) goto exit1
	sw $v0,($t1)		# apothikefse ton akeraio ston pinaka
	add $t2,$t2,1		# plithos akeraiwn ++
	add $t1,$t1,4		# ypologise thn epomenh thesi toy pinaka
	beq $t2,10,exit1	# if (pinakas gematos) goto exit1 
	li $v0,5		# diavase akeraio apo to xrhsth		
	syscall
	j while1

# -------------- EMFANISE TON PINAKA kai YPOLOGISE MEGALYTERH TIMI -------------------
		
exit1:  
	li $t3,1		# $t3 is counter = 1
	la $t1,pin		# $t1 is base register of pin
	bgt $t3,$t2,exit2	# if ($t3 > plithos akeraiwn) goto exit2
	lw $t5,($t1)		# fortwse apo ton pinaka thn prwth timi
	add $s1,$t5,0		# $s1 is max = prwth timi toy pinaka
	move $a0,$t5		# emfanise thn timi
	li $v0,1
	syscall
		
	add $t1,$t1,4		# ypologise thn epomenh thesi toy pinaka
	add $t3,$t3,1		# counter ++
		
	la $a0,keno		# emfanise ena keno xarakthra
	li $v0,4
	syscall
		
		
while2: 
	bgt $t3,$t2,exit2	# if ($t3 > plithos akeraiwn) goto exit2
	lw $t5,($t1)		# fortwse apo ton pinaka mia timi
	move $a0,$t5		# emfanise thn timi
	li $v0,1
	syscall
		
	bge $s1,$t5,synexeia	# if (max >= timi) goto synexeia 
	add $s1,$t5,0		# max = timi
		
synexeia:
	la $a0,keno		# emfanise ena keno xarakthra
	li $v0,4
	syscall
	  
	add $t1,$t1,4		# ypologise thn epomenh thesi toy pinaka
	add $t3,$t3,1		# counter ++

	j while2

# ----------------- EMFANISE TH MEGALYTERH TIMI TOY PINAKA --------------------

exit2:	
	la $a0,enter		# emfanise allagi grammis
	li $v0,4
	syscall
		
	move $a0,$s1		# emfanise max
	li $v0,1
	syscall
		
	li $v0,10		# exit
	syscall
		
		
.data
pin: 	.space 40		# 10 akeraioi
keno:	.asciiz " "
enter:  .asciiz "\n"