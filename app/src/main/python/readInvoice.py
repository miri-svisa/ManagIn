import re


# -----------------------------------------------------------------------
# This function receives text and updates the data array to the data found in the text and returns the array
def main(invoice_text):
    invoice_arr = invoice_text.split('\n')
    data = ["store name", "00/00/00", "0000"]
    data[0] = search_store_name(invoice_arr)
    data[1], data[2] = search_date_and_price(invoice_arr)
    return data


# -----------------------------------------------------------------------
# This function searches the store name
def search_store_name(invoice_arr):
    i = 0
    store_name = invoice_arr[i]
    if re.search(r"\d", store_name):
        store_name = invoice_arr[i + 1]
    if re.search(r"([A-Za-z].*): (.*)", store_name):
        store_name = re.search(r"([A-Za-z].*): (.*)", store_name).group(2)
    if store_name:
        print("store name " + store_name)
        return store_name


# -----------------------------------------------------------------------
# This function searches the date and payment of purchase
def search_date_and_price(invoice_arr):
    for i in invoice_arr:
        purchase_date = re.search(r'\d{1,2}[/.]\d{1,2}[/.]\d{2,4}', i)
        if data[1] == "00/00/00":
            if purchase_date:
                print("purchase date " + purchase_date.group())
                date = purchase_date.group().replace(".", "/")
                data[1] = date

        payment = re.search(r'.*([Aa]mount [Dd]ue|TOTAL|[Ss]umme|[Ss] u m m e)\W*(?!sub)\W*(\d{1,2}[.]\d{1,8})', i)
        if payment:
            print("payment " + payment.group(2))
            price = payment.group(2)
    return date, price
