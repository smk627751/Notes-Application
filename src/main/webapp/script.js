var currentId = 0
class Note{
    constructor({id,title,content,lastEdit})
    {
        this.id = id
        this.title = title
        this.content = content
        this.lastEdit = lastEdit
        this.createNote()
    }
    createNote()
    {
        let div = document.createElement('div')
        div.className = 'note'
        let titleText = document.createElement('span')
        titleText.className = 'title'
        titleText.textContent = this.title
        let contentText = document.createElement('span')
        contentText.className = 'content'
        contentText.innerHTML = this.content
        let timeStamp = document.createElement('span')
        timeStamp.className = 'lastEdit'
        let time = Date.parse(this.lastEdit)
        let f = new Intl.DateTimeFormat(('en-US'),{
            dateStyle:'short',
            timeStyle:'short'
        })
        timeStamp.textContent = f.format(time)
        div.append(titleText,contentText,timeStamp)
        div.addEventListener('click',() => {
            title.value = this.title
            content.innerHTML = this.content
            currentId = this.id
            filename.textContent = this.title
            count.textContent = this.content.length
            lastEdit.textContent = f.format(time)
        })
        document.querySelector('.notes').appendChild(div)
    }
}

let notes = []
let add = document.querySelector('.add')
let search = document.querySelector('#search')
let title = document.querySelector('.textContent .title')
let content = document.querySelector('.textContent .content')
let shareBtn = document.querySelector('.share')
let filename = document.getElementById('filename')
let count = document.getElementById('count')
let lastEdit = document.getElementById('lastEdit')
let formatBtns = document.querySelectorAll('button')

const getNotes = async() => {
    document.querySelector('.notes').innerHTML = ""
    const data = await fetch('/Notes/notes')
    notes = await data.json()
    notes.forEach((note) => {
        let noteObj = new Note(note)
    })
}
getNotes().then(() => {document.querySelector('.note').click()})

const updateNote = async() => {
    await fetch(`/Notes/notes?id=${currentId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ title: title.value,content: content.innerHTML})
    })
    getNotes()
}

const addNote = () => {
    fetch(`/Notes/notes`, {
        method: 'POST'
    })
    getNotes()
}

const deleteNote = async () => {
    await fetch(`/Notes/notes?id=${currentId}`, {
        method: 'DELETE'
    })
    window.location.reload()
}

add.addEventListener('click',() => {
    addNote()
})

search.addEventListener('input',() => {
    const result = notes.filter((note) => {
        return note.title.includes(search.value)
    })
    document.querySelector('.notes').innerHTML = ""
    result.forEach((note) => {
        let noteObj = new Note(note)
    })
})

const formatText = (command) => {
    document.execCommand(command);
}

formatBtns.forEach(btn => {
    btn.addEventListener('click',() => {
        if(btn.id == 'bold')
        {
            formatText('bold');
        }
        if(btn.id == 'italic')
        {
            formatText('italic');
        }
        if(btn.id == 'underline')
        {
            formatText('underline');
        }
        if(btn.id == 'strikeThrough')
        {
            formatText('strikethrough');
        }
        if(btn.id == 'save')
        {
            updateNote()
        }
        if(btn.id == 'delete')
        {
            let status = confirm('Are you sure want to delete?')
            status && deleteNote()
        }
    })
})

shareBtn.addEventListener('click',() => {
    window.open(`https://api.whatsapp.com/send?text=${content.textContent}`,'_blank')
})