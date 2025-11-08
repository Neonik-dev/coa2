export const renderTable = (data, columns) => {
    if (!data || data.length === 0) {
        return <p>Нет данных для отображения</p>
    }

    return (
        <table border="1" style={{ borderCollapse: 'collapse', width: '100%' }}>
            <thead>
            <tr>
                {columns.map(col => (
                    <th key={col.key} style={{ padding: '8px', textAlign: 'left' }}>{col.title}</th>
                ))}
            </tr>
            </thead>
            <tbody>
            {data.map((item, index) => (
                <tr key={index}>
                    {columns.map(col => (
                        <td key={col.key} style={{ padding: '8px' }}>
                            {col.render ? col.render(item[col.dataIndex]) : String(item[col.dataIndex] || '')}
                        </td>
                    ))}
                </tr>
            ))}
            </tbody>
        </table>
    )
}