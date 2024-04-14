#include<stdio.h>
int main ()
{
	int a[2][2],i,j;
	for(j=0;j<2;j++)
	{for(i=0;i<2;i++)
	{printf("enter the [%d][%d] element of metrix",j,i);
	scanf("%d",&a[j][i]);
	}}
	for(j=0;j<2;j++)
	{for(i=0;i<2;i++)
	
	{printf("[%d][%d] element of metrix: %d\n",i,j,a[j][i]);
	}}
	return 0;
}
