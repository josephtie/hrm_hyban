import express, { Request, Response } from 'express'
import cors from 'cors'
import fs from 'fs'
import path from 'path'
import { fileURLToPath } from 'url'
import carbone from 'carbone'

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)

const app = express()
app.use(express.json())
app.use(cors())

const PORT = 3000
const BASE_URL = `http://localhost:${PORT}` 

// Exposer les fichiers générés
app.use('/files', express.static(path.join(__dirname, '../generation')))

app.get('/', (_, res) => {
    res.send('Carbone service running 🚀')
})

// 🔥 API génération bulletin
app.post('/generate-bulletin', (req: Request, res: Response) => {

    const data = req.body

    const templatePath = path.join(__dirname, '../templates/bulletin.xlsx')

    const options = {
        convertTo: 'pdf'
    }

    carbone.render(templatePath, data, options, (err, result) => {

        if (err) {
            console.error('Erreur Carbone:', err)
            return res.status(500).json({ error: 'Erreur génération PDF' })
        }

        const filename = `bulletin-${Date.now()}.pdf` 
        const filePath = path.join(__dirname, `../generation/${filename}`)

        fs.writeFileSync(filePath, result)

        res.json({
            message: 'Bulletin généré',
            url: `${BASE_URL}/files/${filename}` 
        })
    })
})

app.listen(PORT, '0.0.0.0', () => {
    console.log(`🔥 Server started on ${BASE_URL}`)
})
