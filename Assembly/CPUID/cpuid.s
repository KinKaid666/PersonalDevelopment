	.data
vendor_string:
	. = . + 13

	.text
	.global main

main:
	enter	$0, $0

	xor 	%eax, %eax
	xor 	%ebx, %ebx
	xor 	%ecx, %ecx
	xor 	%edx, %edx
	movl	$0, %eax

	cpuid				/* get cpuid name */

	movl	%ebx, vendor_string
	movl	%edx, vendor_string + 4
	movl	%ecx, vendor_string + 8

	pushl	$13
	pushl	$vendor_string
	pushl	$1

	int	$0x80
	addl	$12, %esp

	leave
	ret
