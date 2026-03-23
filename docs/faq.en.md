# FAQ

- Q: How to handle large files?  
  A: Use `poi` engine with streaming writer.
- Q: How to return validation errors?  
  A: Use `ImportResult#getErrors` and write back as an error workbook.
- Q: How to switch engine?  
  A: Pass engine name in `AutoExcel#importFrom/exportTo`.
