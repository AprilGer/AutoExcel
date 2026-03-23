# FAQ

- 问：如何处理大文件？  
  答：使用 `poi` 引擎的流式写入能力。
- 问：如何错误回写？  
  答：使用 `ImportResult#getErrors` 生成错误工作簿。
- 问：如何切换引擎？  
  答：在 `AutoExcel#importFrom/exportTo` 中传入引擎名称。
