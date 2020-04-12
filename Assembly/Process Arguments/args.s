	.data
format:
	.string "Argument %d = '%s'\n"
	len = . - format - 1

	.text
	.global main

main:
	enter	$0, $0

	movl 	$0, %edx
	movl	12(%esp), %ebx

loop:
	pushl	%edx		# save registers
	pushl	%ebx

	pushl	(%ebx)		# push arguments of printf on the stack
	pushl	%edx
	pushl	$format
	call	printf

	addl	$12, %esp	# cleanup the arguments off the stack

	popl	%ebx		# retore the registers
	popl	%edx

	incl	%edx
	addl	$4, %ebx

	cmpl	$0,(%ebx)
	jne	loop

	leave
	ret
