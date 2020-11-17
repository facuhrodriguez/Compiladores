.386
.model flat
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
_z@main DW ? 
_d@main DW ? 
_c@main DW ? 
_b@main DW ? 
_a@main DW ? 
@aux1 DW ? 
_f@main DW ? 
_e@main DW ? 

.code
start: 
MOV CX,f@main
MOV 1,CX
end start