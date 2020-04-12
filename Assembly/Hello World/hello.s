	.data
hello: 
	.asciz "Hello, World\n"
	len = . - hello - 1

	.text
	.global main
main:
	
	enter	$0,$0
	pushl	$len		# push the arguments on the stack
	pushl	$hello
	pushl	$1

	movl	$0, %eax	# write syscall %eax will have ret value
	call 	do_syscall	
	addl	$12, %esp	# cleanup the stack
	
	leave
	ret

do_syscall:
	int	$0x80		# call the kernel
	ret
