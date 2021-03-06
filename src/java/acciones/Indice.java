/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acciones;

import classes.Categoria;
import classes.Historia;
import classes.Noticia;
import classes.Notificacion;
import classes.Tag;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;
import persistencia.CategoriaREST;
import persistencia.HistoriaREST;
import persistencia.NoticiaREST;

import persistencia.NotificacionREST;
import persistencia.TagREST;

/**
 *
 * @author Juanma
 */
public class Indice extends ActionSupport {

    List<Categoria> categorias;
    String categoria;
    List<Noticia> lista;
    List<Noticia> coincidencias = new ArrayList<>();
    List<Historia> coincidenciasHistoria = new ArrayList<>();

    public Indice() {

    }

    int numNoti = 0;
    List<Categoria> listaCat = new ArrayList();
    List<Categoria> listaCategoriaMenu = new ArrayList();

    public List<Categoria> getListaCategoriaMenu() {
        return listaCategoriaMenu;
    }

    public void setListaCategoriaMenu(List<Categoria> listaCategoriaMenu) {
        this.listaCategoriaMenu = listaCategoriaMenu;
    }

    public String execute() throws Exception {

        HistoriaREST hr = new HistoriaREST();
        GenericType<List<Historia>> gh = new GenericType<List<Historia>>() {
        };
        List<Historia> historias = hr.findAll_XML(gh);

        NoticiaREST nor = new NoticiaREST();
        GenericType<List<Noticia>> gt3 = new GenericType<List<Noticia>>() {
        };
        List<Noticia> noticias = nor.findAll_XML(gt3);

        noticias.sort(new Comparator<Noticia>() {
            public int compare(Noticia o1, Noticia o2) {
                return o2.getFechaNoticia().compareTo(o1.getFechaNoticia());
            }
        });

        historias.sort(new Comparator<Historia>() {
            public int compare(Historia o1, Historia o2) {
                return o2.getFechaHistoria().compareTo(o1.getFechaHistoria());
            }
        });

        reducirTam(noticias, historias);

        CategoriaREST cr = new CategoriaREST();
        GenericType<List<Categoria>> gt2 = new GenericType<List<Categoria>>() {
        };
        categorias = cr.findAll_XML(gt2);

        CategoriaREST categoriar = new CategoriaREST();
        GenericType<List<Categoria>> genericCat = new GenericType<List<Categoria>>() {
        };
        listaCategoriaMenu = categoriar.findAll_XML(genericCat);

        Map sessionnotifi = (Map) ActionContext.getContext().get("session");
        NotificacionREST notifir = new NotificacionREST();
        GenericType<List<Notificacion>> gtnotificaciones = new GenericType<List<Notificacion>>() {
        };
        listNot = notifir.findAll_XML(gtnotificaciones);
        for (Notificacion notificacion : listNot) {
            if (notificacion.getNombreUsuario().getNombreUsuario().equals(sessionnotifi.get("usuario"))) {
                listaNotifi.add(notificacion);
            }

        }
        numNoti = listaNotifi.size();

        return SUCCESS;
    }

    /////////////////
    List<Notificacion> listNot = new ArrayList();
    List<Notificacion> listaNotifi = new ArrayList();

    public List<Notificacion> getListaNotifi() {
        return listaNotifi;
    }

    public void setListaNotifi(List<Notificacion> listaNotifi) {
        this.listaNotifi = listaNotifi;
    }

    public int getNumNoti() {
        return numNoti;
    }

    public void setNumNoti(int numNoti) {
        this.numNoti = numNoti;
    }

    public List<Notificacion> getListNot() {
        return listNot;
    }

    public void setList(List<Notificacion> listNot) {
        this.listNot = listNot;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<Noticia> getCoincidencias() {
        return coincidencias;
    }

    public void setCoincidencias(List<Noticia> coincidencias) {
        this.coincidencias = coincidencias;
    }

    public List<Historia> getCoincidenciasHistoria() {
        return coincidenciasHistoria;
    }

    public void setCoincidenciasHistoria(List<Historia> coincidenciasHistoria) {
        this.coincidenciasHistoria = coincidenciasHistoria;
    }

    private void reducirTam(List<Noticia> noticias, List<Historia> historias) {
        int numNoticias = 20;
        int numHistorias = 10;
        for (Noticia n : noticias) {
            if (numNoticias > 0) {
                coincidencias.add(n);
                numNoticias--;
            }
        }

        for (Historia h : historias) {
            if (numHistorias > 0) {
                coincidenciasHistoria.add(h);
                numHistorias--;
            }
        }

    }

}
