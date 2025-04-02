<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h1>${title}</h1>
    <table>
        <tr>
            <th>Image Name</th>
            <th>File Path</th>
        </tr>
        <#list images as image>
        <tr>
            <td>${image.name()}</td>
            <td>${image.path()}</td>
        </tr>
        </#list>
    </table>
</body>
</html>